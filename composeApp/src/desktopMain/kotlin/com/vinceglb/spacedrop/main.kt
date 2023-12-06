package com.vinceglb.spacedrop

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import com.vinceglb.spacedrop.di.composeModule
import com.vinceglb.spacedrop.di.desktopModule
import com.vinceglb.spacedrop.di.initKoin

fun main() = application {
    // Initialize Koin
    initKoin(listOf(composeModule, desktopModule))

    // Launch app
    Window(onCloseRequest = ::exitApplication) {
        App()
    }
}
