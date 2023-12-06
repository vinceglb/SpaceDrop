package com.vinceglb.spacedrop.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable

@Composable
fun SpaceDropTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit,
) {
    MaterialTheme(
        colorScheme = platformColorScheme(darkTheme),
        typography = SpaceDropTypography,
        shapes = SpaceDropShapes,
        content = content
    )
}
