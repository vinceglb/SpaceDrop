@file:Suppress("unused", "FunctionName")

package com.vinceglb.spacedrop

import androidx.compose.ui.window.ComposeUIViewController
import com.vinceglb.spacedrop.di.composeModule
import com.vinceglb.spacedrop.di.composePlatformModule
import com.vinceglb.spacedrop.di.initKoin
import org.koin.compose.KoinContext

fun MainViewController() = ComposeUIViewController {
    KoinContext {
        App()
    }
}

fun initKoinIOS() = initKoin(listOf(composeModule, composePlatformModule))
