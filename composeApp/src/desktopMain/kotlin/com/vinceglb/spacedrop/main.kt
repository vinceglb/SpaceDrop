package com.vinceglb.spacedrop

import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.window.Notification
import androidx.compose.ui.window.Tray
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberTrayState
import com.vinceglb.spacedrop.di.composeModule
import com.vinceglb.spacedrop.di.composePlatformModule
import com.vinceglb.spacedrop.di.desktopModule
import com.vinceglb.spacedrop.di.startAppKoin
import org.koin.compose.KoinApplication

fun main() = application {
    val trayState = rememberTrayState()

    Tray(
        state = trayState,
        icon = TrayIcon,
        menu = {
            Item(
                "Send notification",
                onClick = {
                    trayState.sendNotification(
                        Notification(
                            title = "Hello",
                            message = "World!",
                            type = Notification.Type.Info
                        )
                    )
                }
            )
        }
    )

    val sendNotification = { title: String, message: String ->
        trayState.sendNotification(Notification(title = title, message = message))
    }

    // Launch app
    Window(
        onCloseRequest = ::exitApplication,
    ) {
        window.rootPane.putClientProperty("apple.awt.fullWindowContent", true)
        window.rootPane.putClientProperty("apple.awt.transparentTitleBar", true)
        window.rootPane.putClientProperty("apple.awt.windowTitleVisible", false)

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

object TrayIcon : Painter() {
    override val intrinsicSize = Size(256f, 256f)

    override fun DrawScope.onDraw() {
        drawOval(Color(0xFFFFA500))
    }
}
