package com.vinceglb.spacedrop.ui.screens.permission

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import co.touchlab.kermit.Logger
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import platform.UserNotifications.UNAuthorizationOptionAlert
import platform.UserNotifications.UNAuthorizationOptionBadge
import platform.UserNotifications.UNAuthorizationOptionSound
import platform.UserNotifications.UNAuthorizationStatus
import platform.UserNotifications.UNAuthorizationStatusAuthorized
import platform.UserNotifications.UNAuthorizationStatusDenied
import platform.UserNotifications.UNAuthorizationStatusNotDetermined
import platform.UserNotifications.UNUserNotificationCenter
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

@Composable
actual fun isAllPermissionsGranted(): Boolean {
    var permissionStatus by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        while (true) {
            permissionStatus = notificationPermissionStatus() == UNAuthorizationStatusAuthorized
            delay(1_000)
        }
    }

    return permissionStatus
}

@Composable
actual fun NotificationPermissionButton() {
    val scope = rememberCoroutineScope()
    var isLoading by remember { mutableStateOf(false) }

    fun request() {
        scope.launch {
            isLoading = true
            val isSuccess = requestNotificationPermission()
            if (!isSuccess) {
                isLoading = false
            }
        }
    }

    Button(onClick = ::request) {
        if (isLoading) {
            CircularProgressIndicator(modifier = Modifier.size(24.dp))
            Spacer(modifier = Modifier.size(8.dp))
        }
        Text(text = "Notification permission")
    }
}

suspend fun notificationPermissionStatus(): UNAuthorizationStatus {
    val currentCenter = UNUserNotificationCenter.currentNotificationCenter()

    return suspendCoroutine { continuation ->
        currentCenter.getNotificationSettingsWithCompletionHandler { settings ->
            val status = settings?.authorizationStatus ?: UNAuthorizationStatusNotDetermined
            continuation.resume(status)
        }
    }
}

suspend fun requestNotificationPermission(): Boolean {
    // Notification Center
    val currentCenter = UNUserNotificationCenter.currentNotificationCenter()

    // Notification permission status
    val status = notificationPermissionStatus()

    // Request notification permission
    return when (status) {
        UNAuthorizationStatusNotDetermined -> {
            Logger.i(TAG) { "Requesting notification permission" }
            val isSuccess = suspendCoroutine { continuation ->
                currentCenter.requestAuthorizationWithOptions(
                    UNAuthorizationOptionSound or UNAuthorizationOptionAlert or UNAuthorizationOptionBadge,
                ) { isOk, error ->
                    if (isOk && error == null) {
                        continuation.resumeWith(Result.success(true))
                    } else {
                        continuation.resumeWith(Result.success(false))
                    }
                }
            }

            if (isSuccess) {
                Logger.i(TAG) { "Notification permission granted" }
            } else {
                Logger.e(TAG) { "Notification permission not granted" }
            }
            true
        }

        UNAuthorizationStatusAuthorized -> {
            Logger.i(TAG) { "UNAuthorizationStatusAuthorized: Notification permission already granted" }
            true
        }

        UNAuthorizationStatusDenied -> {
            Logger.w(TAG) { "UNAuthorizationStatusDenied: Notification permission not granted" }
            false
        }

        else -> {
            Logger.e(TAG) { "Notification permission not granted: null" }
            false
        }
    }
}

private const val TAG = "PermissionScreen.apple"
