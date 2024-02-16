package com.vinceglb.spacedrop.data.repository

import com.ionspin.kotlin.crypto.LibsodiumInitializer
import com.ionspin.kotlin.crypto.box.Box
import com.ionspin.kotlin.crypto.box.BoxKeyPair
import com.ionspin.kotlin.crypto.pwhash.PasswordHash
import com.ionspin.kotlin.crypto.pwhash.crypto_pwhash_ALG_DEFAULT
import com.ionspin.kotlin.crypto.pwhash.crypto_pwhash_MEMLIMIT_INTERACTIVE
import com.ionspin.kotlin.crypto.pwhash.crypto_pwhash_OPSLIMIT_INTERACTIVE
import com.ionspin.kotlin.crypto.pwhash.crypto_pwhash_SALTBYTES
import com.ionspin.kotlin.crypto.secretbox.SecretBox
import com.ionspin.kotlin.crypto.secretbox.crypto_secretbox_NONCEBYTES
import com.ionspin.kotlin.crypto.util.LibsodiumRandom
import com.ionspin.kotlin.crypto.util.decodeFromUByteArray
import com.ionspin.kotlin.crypto.util.encodeToUByteArray
import com.ionspin.kotlin.crypto.util.hexStringToUByteArray
import com.ionspin.kotlin.crypto.util.toHexString
import com.vinceglb.spacedrop.data.settings.SecretLocalDataSource
import com.vinceglb.spacedrop.data.supabase.SecretRemoteDataSource
import com.vinceglb.spacedrop.model.Secret
import com.vinceglb.spacedrop.model.SecretCreateRequest
import io.ktor.util.decodeBase64String
import io.ktor.util.encodeBase64
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@OptIn(ExperimentalUnsignedTypes::class)
class SecretRepository(
    private val secretRemoteDataSource: SecretRemoteDataSource,
    private val secretLocalDataSource: SecretLocalDataSource,
    private val authRepository: AuthRepository,
    private val applicationScope: CoroutineScope,
) {
    fun getKeyPair(): Flow<BoxKeyPair?> =
        secretLocalDataSource.getSecret()

    suspend fun fetchSecret(): Secret? {
        return authRepository.getCurrentUser().first()?.let { user ->
            secretRemoteDataSource.fetchSecret(user.id)
        }
    }

    suspend fun createSecret(password: String) {
        applicationScope.launch(Dispatchers.Default) {
            // Initialize Libsodium
            LibsodiumInitializer.initialize()

            // Generate the user secret create request
            val createRequest = encryption(password)

            // Create the user secret
            val userSecret = secretRemoteDataSource.createSecret(createRequest)

            // Save the user secret locally
            decryptSecret(password, userSecret)
        }
    }

    suspend fun decryptSecret(password: String, secret: Secret) {
        applicationScope.launch(Dispatchers.Default) {
            // Initialize Libsodium
            LibsodiumInitializer.initialize()

            val keyPair = decryption(password, secret)
            secretLocalDataSource.saveSecret(keyPair)
        }
    }

    suspend fun encryptMessage(message: String): String {
        // Initialize Libsodium
        LibsodiumInitializer.initialize()

        val keyPair = getKeyPair().firstOrNull()
            ?: throw IllegalStateException("No key pair found")

        return Box.seal(
            message = message.encodeToUByteArray(),
            recipientsPublicKey = keyPair.publicKey
        ).toHexString()
    }

    suspend fun decryptMessage(ciphertext: String): String {
        // Initialize Libsodium
        LibsodiumInitializer.initialize()

        val keyPair = getKeyPair().firstOrNull()
            ?: throw IllegalStateException("No key pair found")

        return Box.sealOpen(
            ciphertext = ciphertext.hexStringToUByteArray(),
            recipientsPublicKey = keyPair.publicKey,
            recipientsSecretKey = keyPair.secretKey
        ).decodeFromUByteArray()
    }

    suspend fun checkPassword(password: String, secret: Secret): Boolean =
        withContext(Dispatchers.Default) {
            // Initialize Libsodium
            LibsodiumInitializer.initialize()

            PasswordHash.strVerify(
                passwordHash = secret.passwordHash.decodeBase64String(),
                password = password
            )
        }

    suspend fun logout() {
        applicationScope.launch {
            secretLocalDataSource.clearSecret()
            authRepository.signOut()
        }
    }

    private fun encryption(password: String): SecretCreateRequest {
        val salt = LibsodiumRandom.buf(crypto_pwhash_SALTBYTES)

        val passwordKeyDerivation = PasswordHash.pwhash(
            outputLength = 32,
            password = password,
            salt = salt,
            opsLimit = crypto_pwhash_OPSLIMIT_INTERACTIVE.toULong(),
            memLimit = crypto_pwhash_MEMLIMIT_INTERACTIVE,
            algorithm = crypto_pwhash_ALG_DEFAULT,
        )

        val passwordHash = PasswordHash.str(
            password = password,
            opslimit = crypto_pwhash_OPSLIMIT_INTERACTIVE.toULong(),
            memlimit = crypto_pwhash_MEMLIMIT_INTERACTIVE,
        )

        val keyPair = Box.keypair()

        val privateKeyNonce = LibsodiumRandom.buf(crypto_secretbox_NONCEBYTES)

        val privateKeyEncrypted = SecretBox.easy(
            message = keyPair.secretKey,
            nonce = privateKeyNonce,
            key = passwordKeyDerivation
        )

        return SecretCreateRequest(
            passwordHash = passwordHash.encodeBase64(),
            salt = salt.toHexString(),
            publicKey = keyPair.publicKey.toHexString(),
            secretKeyNonce = privateKeyNonce.toHexString(),
            secretKeyEncrypted = privateKeyEncrypted.toHexString(),
        )
    }

    private fun decryption(password: String, secret: Secret): BoxKeyPair {
        val isPasswordValid = PasswordHash.strVerify(
            passwordHash = secret.passwordHash.decodeBase64String(),
            password = password
        )

        if (!isPasswordValid) {
            throw Exception("Invalid password")
        }

        val passwordKeyDerivation = PasswordHash.pwhash(
            outputLength = 32,
            password = password,
            salt = secret.salt.hexStringToUByteArray(),
            opsLimit = crypto_pwhash_OPSLIMIT_INTERACTIVE.toULong(),
            memLimit = crypto_pwhash_MEMLIMIT_INTERACTIVE,
            algorithm = crypto_pwhash_ALG_DEFAULT,
        )

        val privateKey = SecretBox.openEasy(
            ciphertext = secret.secretKeyEncrypted.hexStringToUByteArray(),
            nonce = secret.secretKeyNonce.hexStringToUByteArray(),
            key = passwordKeyDerivation
        )

        return BoxKeyPair(
            publicKey = secret.publicKey.hexStringToUByteArray(),
            secretKey = privateKey
        )
    }
}
