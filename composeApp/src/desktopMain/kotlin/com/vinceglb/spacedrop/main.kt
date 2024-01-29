package com.vinceglb.spacedrop

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
import com.vinceglb.spacedrop.di.composeModule
import com.vinceglb.spacedrop.di.composePlatformModule
import com.vinceglb.spacedrop.di.desktopModule
import com.vinceglb.spacedrop.di.startAppKoin
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.painterResource
import org.koin.compose.KoinApplication
import spacedrop.composeapp.generated.resources.Res
import java.awt.Window

@OptIn(ExperimentalResourceApi::class)
fun main() = application {
    var isWindowVisible by remember { mutableStateOf(true) }
    val windowState = rememberWindowState()
    var appWindow: Window? = null

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
                    appWindow?.toFront()
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

}
