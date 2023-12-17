package com.vinceglb.spacedrop.service

import co.touchlab.kermit.Logger
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class SpaceDropMessagingService : FirebaseMessagingService() {
    companion object {
        private const val TAG = "SpaceDropMessagingService"
    }

    override fun onNewToken(token: String) {
        Logger.i(TAG) { "FCM registration token: $token" }
    }

    override fun onMessageReceived(message: RemoteMessage) {
        Logger.i(TAG) { "Message received from: ${message.from}" }
    }
}
