package com.vinceglb.spacedrop.ui.screens.home

import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import com.vinceglb.spacedrop.data.repository.AuthRepository
import com.vinceglb.spacedrop.data.repository.DeviceRepository
import com.vinceglb.spacedrop.data.repository.EventRepository
import com.vinceglb.spacedrop.data.repository.SecretRepository
import com.vinceglb.spacedrop.model.AuthUser
import com.vinceglb.spacedrop.model.Device
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class HomeScreenModel(
    private val authRepository: AuthRepository,
    private val deviceRepository: DeviceRepository,
    private val eventRepository: EventRepository,
    private val secretRepository: SecretRepository,
) : ScreenModel {
    val uiState: StateFlow<HomeScreenUiState> = combine(
        authRepository.getCurrentUser(),
        deviceRepository.getCurrentDevice(),
        deviceRepository.getDevices(),
        eventRepository.getDeviceEvents(),
        eventRepository.getMessage(),
    ) { currentUser, currentDevice, devices, events, message ->
        if (events.isNotEmpty()) {
            eventRepository.executeEvents()
        }

        HomeScreenUiState(
            currentUser = currentUser,
            currentDevice = currentDevice,
            devices = devices,
            message = message,
        )
    }.stateIn(
        scope = screenModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = HomeScreenUiState(),
    )

    fun signOut() {
        screenModelScope.launch {
            secretRepository.logout()
        }
    }

    fun deleteDevice(deviceId: String) {
        screenModelScope.launch {
            deviceRepository.deleteDevice(deviceId)
        }
    }

    fun renameDevice(deviceId: String, newName: String) {
        screenModelScope.launch {
            deviceRepository.renameDevice(deviceId, newName)
        }
    }

    fun sendNotificationEvent(destinationDeviceId: String, text: String) {
        screenModelScope.launch {
            if (text.isNotBlank()) {
                // Check if the text is a URL
                val urlRegex = Regex("https?://\\S+")
                if (urlRegex.matches(text)) {
                    eventRepository.sendUrlEvent(destinationDeviceId, text)
                } else {
                    eventRepository.sendTextEvent(destinationDeviceId, text)
                }
            } else {
                eventRepository.sendNotificationEvent(destinationDeviceId)
            }
        }
    }

    fun consumeMessage() {
        screenModelScope.launch {
            eventRepository.consumeMessage()
        }
    }
}

data class HomeScreenUiState(
    val currentUser: AuthUser? = null,
    val currentDevice: Device? = null,
    val devices: List<Device> = emptyList(),
    val message: String? = null,
)
