package com.vinceglb.spacedrop.ui.components

import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.vinceglb.spacedrop.model.Platform
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.painterResource

@OptIn(ExperimentalResourceApi::class)
@Composable
fun PlatformIcon(
    platform: Platform,
    modifier: Modifier = Modifier,
    tint: Color = LocalContentColor.current,
) {
    when (platform) {
        Platform.Android -> Icon(
            painterResource("icons/android_logo.xml"),
            contentDescription = "Android Logo",
            tint = tint,
            modifier = modifier
        )
        Platform.IOS -> Icon(
            painterResource("icons/apple_logo.xml"),
            contentDescription = "IOS Logo",
            tint = tint,
            modifier = modifier
        )
        Platform.Linux -> Icon(
            painterResource("icons/linux_logo.xml"),
            contentDescription = "Linux Logo",
            tint = tint,
            modifier = modifier
        )
        Platform.MacOS -> Icon(
            painterResource("icons/apple_logo.xml"),
            contentDescription = "MacOS Logo",
            tint = tint,
            modifier = modifier
        )
        Platform.Windows -> Icon(
            painterResource("icons/windows_logo.xml"),
            contentDescription = "Windows Logo",
            tint = tint,
            modifier = modifier
        )
    }
}
