package com.vinceglb.spacedrop.ui.screens.permission

import android.os.Build
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import co.touchlab.kermit.Logger
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState

@OptIn(ExperimentalPermissionsApi::class)
@Composable
actual fun isAllPermissionsGranted(): Boolean {
    // Notification permission state
    val notificationState = notificationPermission()
    return notificationState == null || notificationState.status.isGranted
}

@OptIn(ExperimentalPermissionsApi::class)
@Composable
actual fun NotificationPermissionButton() {
    val notificationState = notificationPermission()

    if (notificationState == null) {
        Logger.e("PermissionScreen.android") { "Notification permission state should not be null" }
        Text(text = "Notification permission state should not be null")
        return
    }

    Button(onClick = notificationState::launchPermissionRequest) {
        Text(text = "Notification permission")
    }
}

@OptIn(ExperimentalPermissionsApi::class)
@Composable
private fun notificationPermission() = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
    rememberPermissionState(android.Manifest.permission.POST_NOTIFICATIONS)
} else {
    null
}
