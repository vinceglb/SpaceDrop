package com.vinceglb.spacedrop.ui.theme

import androidx.compose.material3.ColorScheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

@Composable
actual fun platformColorScheme(darkTheme: Boolean): ColorScheme {
     return when {
         darkTheme -> darkColorScheme()
         else -> lightColorScheme()
     }
}
