package com.vinceglb.spacedrop.data.platform

import co.touchlab.kermit.Logger
import platform.Foundation.NSUUID
import platform.UserNotifications.UNMutableNotificationContent
import platform.UserNotifications.UNNotificationRequest
import platform.UserNotifications.UNTimeIntervalNotificationTrigger
import platform.UserNotifications.UNUserNotificationCenter
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

actual class PlatformActionDataSource {
    actual suspend fun sendNotification() {
        val content = UNMutableNotificationContent().apply {
            setTitle("SpaceDrop")
            setBody("This is a notification")
        }

        val trigger = UNTimeIntervalNotificationTrigger.triggerWithTimeInterval(5.0, false)
        val identifier = NSUUID().UUIDString
        val request = UNNotificationRequest.requestWithIdentifier(identifier, content, trigger)

        suspendCoroutine { continuation ->
            UNUserNotificationCenter.currentNotificationCenter().addNotificationRequest(request) {
                if (it != null) {
                    Logger.e("PlatformActionDataSource") { "Error: $it" }
                }
                continuation.resume(Unit)
            }
        }
    }
}