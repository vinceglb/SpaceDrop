package com.vinceglb.spacedrop

import androidx.compose.runtime.Composable
import cafe.adriel.voyager.navigator.Navigator
import com.vinceglb.spacedrop.ui.screens.MainScreen
import com.vinceglb.spacedrop.ui.theme.SpaceDropTheme

@Composable
fun App() {
    SpaceDropTheme {
        Navigator(MainScreen)
    }
}
