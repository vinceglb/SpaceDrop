package com.vinceglb.spacedrop.util

import com.vinceglb.spacedrop.data.firebase.firebaseToken
import com.vinceglb.spacedrop.data.repository.DeviceRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

// TODO refactor?
object MessagingUtil : KoinComponent {
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
}
