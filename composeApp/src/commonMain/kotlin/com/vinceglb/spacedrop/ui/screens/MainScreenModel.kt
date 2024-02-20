package com.vinceglb.spacedrop.ui.screens

import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import com.vinceglb.spacedrop.data.repository.AuthRepository
import com.vinceglb.spacedrop.data.repository.DeviceRepository
import com.vinceglb.spacedrop.data.repository.EventRepository
import com.vinceglb.spacedrop.data.repository.SecretRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn

class MainScreenModel(
    authRepository: AuthRepository,
    eventRepository: EventRepository,
    deviceRepository: DeviceRepository,
    secretRepository: SecretRepository,
) : ScreenModel {
    val uiState: StateFlow<MainState> = combine(
        authRepository.getCurrentUser(),
        deviceRepository.getCurrentDevice(),
        eventRepository.getNotificationEventId(),
        secretRepository.hasSecret(),
    ) { currentUser, currentDevice, notificationEventId, hasSecret ->
        MainState.Success(
            isLogged = currentUser != null,
            hasSecret = hasSecret,
            isRegistered = currentDevice != null,
            notificationEventId = notificationEventId,
        )
    }.stateIn(
        scope = screenModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = MainState.Loading,
    )
}

sealed class MainState {
    data object Loading : MainState()
    data class Success(
        val isLogged: Boolean,
        val hasSecret: Boolean,
        val isRegistered: Boolean,
        val notificationEventId: String?,
        val message: String? = null,
    ) : MainState()
}
