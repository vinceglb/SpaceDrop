package com.vinceglb.spacedrop.data.settings

import com.ionspin.kotlin.crypto.box.BoxKeyPair
import com.ionspin.kotlin.crypto.util.hexStringToUByteArray
import com.russhwolf.settings.ExperimentalSettingsApi
import com.russhwolf.settings.coroutines.FlowSettings
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.shareIn

interface SecretLocalDataSource {
    fun getSecret(): Flow<BoxKeyPair?>

    suspend fun saveSecret(secret: BoxKeyPair)

    suspend fun clearSecret()
}

@OptIn(
    ExperimentalSettingsApi::class,
    ExperimentalUnsignedTypes::class,
    ExperimentalStdlibApi::class
)
class SecretLocalDataSourcePreferences(
    private val settings: FlowSettings,
    applicationScope: CoroutineScope,
) : SecretLocalDataSource {
    private val secret: SharedFlow<BoxKeyPair?> =
        combine(
            settings.getStringOrNullFlow(SECRET_PUBLIC_KEY),
            settings.getStringOrNullFlow(SECRET_PRIVATE_KEY),
        ) { publicKey, privateKey ->
            if (publicKey != null && privateKey != null) {
                BoxKeyPair(
                    publicKey = publicKey.hexStringToUByteArray(),
                    secretKey = privateKey.hexStringToUByteArray()
                )
            } else {
                null
            }
        }.shareIn(
            scope = applicationScope,
            started = SharingStarted.WhileSubscribed(),
            replay = 1,
        )

    override fun getSecret(): Flow<BoxKeyPair?> =
        secret

    override suspend fun saveSecret(secret: BoxKeyPair) {
        settings.putString(SECRET_PUBLIC_KEY, secret.publicKey.toHexString())
        settings.putString(SECRET_PRIVATE_KEY, secret.secretKey.toHexString())
    }

    override suspend fun clearSecret() {
        settings.remove(SECRET_PUBLIC_KEY)
        settings.remove(SECRET_PRIVATE_KEY)
    }

    private companion object {
        const val SECRET_PUBLIC_KEY = "SECRET_PUBLIC_KEY"
        const val SECRET_PRIVATE_KEY = "SECRET_PRIVATE_KEY"
    }
}
