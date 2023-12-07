package com.vinceglb.spacedrop

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application

fun main() = application {
    // Launch app
    Window(onCloseRequest = ::exitApplication) {
        App()
    }
}
