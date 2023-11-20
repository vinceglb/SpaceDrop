package com.vinceglb.spacedrop

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.runtime.Composable
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import com.vinceglb.spacedrop.di.composeModule
import com.vinceglb.spacedrop.di.initKoin

fun main() = application {
    // Initialize Koin
    initKoin(listOf(composeModule))

    // Launch app
    Window(onCloseRequest = ::exitApplication) {
        App()
    }
}

@Preview
@Composable
fun AppDesktopPreview() {
    App()
}
