package com.vinceglb.spacedrop

import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.window.Notification
import androidx.compose.ui.window.Tray
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberTrayState
import androidx.compose.ui.window.rememberWindowState
import com.vinceglb.spacedrop.composeapp.generated.resources.Res
import com.vinceglb.spacedrop.composeapp.generated.resources.tray_icon
import com.vinceglb.spacedrop.di.composeModule
import com.vinceglb.spacedrop.di.composePlatformModule
import com.vinceglb.spacedrop.di.desktopModule
import com.vinceglb.spacedrop.di.startAppKoin
import kotlinx.coroutines.delay
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.painterResource
import org.koin.compose.KoinApplication
import java.awt.Window

@OptIn(ExperimentalResourceApi::class)
fun main() = application {
    var isWindowVisible by remember { mutableStateOf(true) }
    val windowState = rememberWindowState()
    var appWindow: Window? by remember { mutableStateOf(null) }
    var focus by remember { mutableStateOf(false) }

    val trayState = rememberTrayState()
    val sendNotification = remember {
        { title: String, message: String ->
            trayState.sendNotification(Notification(title = title, message = message))
        }
    }

    Tray(
        state = trayState,
        icon = painterResource(Res.drawable.tray_icon),
        menu = {
            Item(
                "Open SpaceDrop",
                onClick = {
                    isWindowVisible = true
                    focus = true
                },
            )

            Item(
                "Quit",
                onClick = ::exitApplication,
            )
        }
    )

    // Launch app
    Window(
        title = "SpaceDrop",
        state = windowState,
        visible = isWindowVisible,
        alwaysOnTop = focus,
        onCloseRequest = { isWindowVisible = false },
    ) {
        window.apply {
            appWindow = this
            rootPane.putClientProperty("apple.awt.fullWindowContent", true)
            rootPane.putClientProperty("apple.awt.transparentTitleBar", true)
            rootPane.putClientProperty("apple.awt.windowTitleVisible", false)
        }

        KoinApplication(
            application = {
                startAppKoin(
                    listOf(
                        composeModule,
                        composePlatformModule,
                        desktopModule(sendNotification)
                    )
                )
            }
        ) {
            App()
        }
    }

    LaunchedEffect(focus) {
        if (focus) {
            delay(1200)
            appWindow?.toFront()
            delay(1200)
            focus = false
        }
    }

//    LaunchedEffect(Unit) {
//        LibsodiumInitializer.initialize()
//
//        val message = "Hello, World!".encodeToUByteArray()
//
//        val aliceKeyPair = Box.keypair()
//        val bobKeyPair = Box.keypair()
//
//        val messageNonce = Random(0).nextUBytes(crypto_box_NONCEBYTES)
//
//        val encrypted = Box.easy(
//            message = message,
//            nonce = messageNonce,
//            recipientsPublicKey = bobKeyPair.publicKey,
//            sendersSecretKey = aliceKeyPair.secretKey,
//        )
//
//        val sharedKey = Box.beforeNM(
//            publicKey = bobKeyPair.publicKey,
//            secretKey = aliceKeyPair.secretKey
//        )
//
//        val res = Box.openEasyAfterNM(
//            ciphertext = encrypted,
//            nonce = messageNonce,
//            precomputedKey = sharedKey,
//        )
//
//        Logger.d { "Encrypted message: ${encrypted.toHexString()}" }
//
//        val decrypted = Box.openEasy(
//            ciphertext = encrypted,
//            nonce = messageNonce,
//            sendersPublicKey = aliceKeyPair.publicKey,
//            recipientsSecretKey = bobKeyPair.secretKey,
//        )
//
//        Logger.d { "Decrypted message: ${decrypted.decodeFromUByteArray()}" }
//        Logger.d { "Decrypted message Res: ${res.decodeFromUByteArray()}" }
//
//        val encrypted2 = Box.seal(
//            message = message,
//            recipientsPublicKey = bobKeyPair.publicKey
//        )
//
//        Logger.d { "Encrypted message 2: ${encrypted2.toHexString()}" }
//
//        val decrypted2 = Box.sealOpen(
//            ciphertext = encrypted2,
//            recipientsPublicKey = bobKeyPair.publicKey,
//            recipientsSecretKey = bobKeyPair.secretKey
//        )
//
//        Logger.d { "Decrypted message 2: ${decrypted2.decodeFromUByteArray()}" }
//
//        val password = "Test1234"
//        val salt = "vince".encodeToUByteArray() // LibsodiumRandom.buf(crypto_pwhash_SALTBYTES)
//
//        val passwordHash = PasswordHash.pwhash(
//            outputLength = 16,
//            password = password,
//            salt = salt,
//            opsLimit = crypto_pwhash_OPSLIMIT_INTERACTIVE.toULong(),
//            memLimit = crypto_pwhash_MEMLIMIT_INTERACTIVE,
//            algorithm = crypto_pwhash_ALG_DEFAULT,
//        )
//
//        // ce3b7b978b3c5f11e9dbd96f2e6f5961
//        Logger.d { "Password hash: ${passwordHash.toHexString()}" }
//
////        PasswordHash.
//
//        // val masterKey = Kdf.keygen()
//        // Kdf.
//    }
}
