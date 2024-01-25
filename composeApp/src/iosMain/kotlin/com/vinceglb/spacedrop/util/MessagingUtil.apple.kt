package com.vinceglb.spacedrop.util

import com.vinceglb.spacedrop.data.firebase.firebaseToken
import com.vinceglb.spacedrop.data.repository.DeviceRepository
import com.vinceglb.spacedrop.data.repository.EventRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

// TODO refactor?
object MessagingUtil : KoinComponent {
    private val eventRepository: EventRepository by inject()

    fun updateFcmToken(token: String) {
        // Get dependencies
        val deviceRepository: DeviceRepository by inject()
        val applicationScope: CoroutineScope by inject()

        // Update the token
        applicationScope.launch {
            deviceRepository.updateFcmToken(token)
        }

        // Hack for now
        firebaseToken = token
    }

    fun onNotificationEventId(eventId: String) {
        val applicationScope: CoroutineScope by inject()

        applicationScope.launch {
            eventRepository.onNotificationEventId(eventId)
        }
    }

//    fun executeEvents() {
//        // Get dependencies
//        val eventRepository: EventRepository by inject()
//        val applicationScope: CoroutineScope by inject()
//
//        // Update the token
//        applicationScope.launch {
//            eventRepository.executeEvents()
//        }
//    }
}
