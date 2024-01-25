package com.vinceglb.spacedrop.ui.screens

import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import com.vinceglb.spacedrop.data.repository.AuthRepository
import com.vinceglb.spacedrop.data.repository.DeviceRepository
import com.vinceglb.spacedrop.data.repository.EventRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn

class MainScreenModel(
    authRepository: AuthRepository,
    eventRepository: EventRepository,
    deviceRepository: DeviceRepository,
) : ScreenModel {
    val uiState: StateFlow<MainState> = combine(
        authRepository.getCurrentUser(),
        deviceRepository.getCurrentDevice(),
        eventRepository.getNotificationEventId(),
    ) { currentUser, currentDevice, notificationEventId ->
        MainState.Success(
            isLogged = currentUser != null,
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
        val isRegistered: Boolean,
        val notificationEventId: String?
    ) : MainState()
}
