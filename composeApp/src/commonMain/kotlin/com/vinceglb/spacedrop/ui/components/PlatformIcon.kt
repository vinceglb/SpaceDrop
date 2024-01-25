package com.vinceglb.spacedrop.ui.components

import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.vinceglb.spacedrop.model.Platform
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.painterResource
import spacedrop.composeapp.generated.resources.Res

@OptIn(ExperimentalResourceApi::class)
@Composable
fun PlatformIcon(
    platform: Platform,
    modifier: Modifier = Modifier,
    tint: Color = LocalContentColor.current,
) {
    when (platform) {
        Platform.Android -> Icon(
            painterResource(Res.drawable.android_logo),
            contentDescription = "Android Logo",
            tint = tint,
            modifier = modifier
        )
        Platform.IOS -> Icon(
            painterResource(Res.drawable.apple_logo),
            contentDescription = "IOS Logo",
            tint = tint,
            modifier = modifier
        )
        Platform.Linux -> Icon(
            painterResource(Res.drawable.linux_logo),
            contentDescription = "Linux Logo",
            tint = tint,
            modifier = modifier
        )
        Platform.MacOS -> Icon(
            painterResource(Res.drawable.apple_logo),
            contentDescription = "MacOS Logo",
            tint = tint,
            modifier = modifier
        )
        Platform.Windows -> Icon(
            painterResource(Res.drawable.windows_logo),
            contentDescription = "Windows Logo",
            tint = tint,
            modifier = modifier
        )
    }
}
