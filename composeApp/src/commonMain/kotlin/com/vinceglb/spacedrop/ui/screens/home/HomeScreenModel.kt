package com.vinceglb.spacedrop.ui.screens.home

import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import com.vinceglb.spacedrop.data.repository.AuthRepository
import com.vinceglb.spacedrop.data.repository.DeviceRepository
import com.vinceglb.spacedrop.model.AuthUser
import com.vinceglb.spacedrop.model.Device
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn

class HomeScreenModel(
    authRepository: AuthRepository,
    deviceRepository: DeviceRepository,
) : ScreenModel {
    val uiState: StateFlow<HomeScreenUiState> = combine(
        authRepository.getCurrentUser(),
        deviceRepository.getCurrentDevice(),
        deviceRepository.getDevices(),
    ) { currentUser, currentDevice, devices ->
        HomeScreenUiState(
            currentUser = currentUser,
            currentDevice = currentDevice,
            devices = devices,
        )
    }.stateIn(
        scope = screenModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = HomeScreenUiState(),
    )
}

data class HomeScreenUiState(
    val currentUser: AuthUser? = null,
    val currentDevice: Device? = null,
    val devices: List<Device> = emptyList(),
)
