package com.vinceglb.spacedrop

import androidx.compose.runtime.Composable
import cafe.adriel.voyager.navigator.Navigator
import co.touchlab.kermit.Logger
import co.touchlab.kermit.koin.KermitKoinLogger
import com.vinceglb.spacedrop.di.commonModule
import com.vinceglb.spacedrop.di.composeModule
import com.vinceglb.spacedrop.di.composePlatformModule
import com.vinceglb.spacedrop.ui.screens.MainScreen
import com.vinceglb.spacedrop.ui.theme.SpaceDropTheme
import org.koin.compose.KoinApplication

@Composable
fun App() {
    KoinApplication(
        application = {
            // Logger
            logger(KermitKoinLogger(Logger.withTag("Koin")))

            // Modules
            modules(listOf(composeModule, composePlatformModule, commonModule))
        }
    ) {
        SpaceDropTheme {
            Navigator(MainScreen)
        }
    }
}
