package com.vinceglb.spacedrop.service

import co.touchlab.kermit.Logger
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.vinceglb.spacedrop.data.repository.DeviceRepository
import com.vinceglb.spacedrop.data.repository.EventRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject

class SpaceDropMessagingService : FirebaseMessagingService() {
    private val eventRepository: EventRepository by inject()
    private val deviceRepository: DeviceRepository by inject()
    private val applicationScope: CoroutineScope by inject()

    override fun onNewToken(token: String) {
        Logger.i(TAG) { "FCM registration token: $token" }

        applicationScope.launch {
            deviceRepository.updateFcmToken(token)
        }
    }

    override fun onMessageReceived(message: RemoteMessage) {
        Logger.i(TAG) { "Message received from: ${message.from}" }

        // Check if message contains a data payload.
//        message.data.isNotEmpty().let {
//            val id = message.data["id"] ?: ""
//            applicationScope.launch {
//                Logger.i(TAG) { "Message data payload: $id / ${message.data}" }
//                eventRepository.executeEvents()
//            }
//        }
    }

    companion object {
        private const val TAG = "SpaceDropMessagingService"
    }
}
