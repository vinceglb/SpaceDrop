package com.vinceglb.spacedrop

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import com.vinceglb.spacedrop.di.composeModule
import com.vinceglb.spacedrop.di.composePlatformModule
import com.vinceglb.spacedrop.di.startAppKoin
import org.koin.compose.KoinApplication

fun main() = application {
    // Launch app
    Window(onCloseRequest = ::exitApplication) {
        KoinApplication(
            application = { startAppKoin(listOf(composeModule, composePlatformModule)) }
        ) {
            App()
        }
    }
}
