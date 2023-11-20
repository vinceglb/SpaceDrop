package com.vinceglb.spacedrop

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import cafe.adriel.voyager.navigator.Navigator
import com.vinceglb.spacedrop.ui.HomeScreen

@Composable
fun App() {
    MaterialTheme {
        Navigator(HomeScreen())
    }
}
