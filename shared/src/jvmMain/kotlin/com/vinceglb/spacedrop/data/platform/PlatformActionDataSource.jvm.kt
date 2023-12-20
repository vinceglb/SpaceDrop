package com.vinceglb.spacedrop.data.platform

import com.vinceglb.spacedrop.util.SendNotification

actual class PlatformActionDataSource(
    private val onSendNotification: SendNotification,
) {
    actual suspend fun sendNotification() {
        onSendNotification("SpaceDrop", "This is a notification")
    }
}
