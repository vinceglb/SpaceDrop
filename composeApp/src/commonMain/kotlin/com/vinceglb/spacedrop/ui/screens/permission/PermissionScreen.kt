package com.vinceglb.spacedrop.ui.screens.permission

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import com.vinceglb.spacedrop.ui.components.OnboardingHeader
import com.vinceglb.spacedrop.ui.components.OnboardingLayout

object PermissionScreen : Screen {
    @Composable
    override fun Content() {
        PermissionScreen()
    }
}

@Composable
private fun PermissionScreen() {
    OnboardingLayout {
        OnboardingHeader(
            icon = Icons.Default.Info,
            iconDescription = "Permission",
            title = "Permission Screen",
            subtitle = "This is the permission screen of SpaceDrop.",
            modifier = Modifier.padding(bottom = 32.dp)
        )

        NotificationPermissionButton()
    }
}

@Composable
expect fun isAllPermissionsGranted(): Boolean

@Composable
expect fun NotificationPermissionButton()
