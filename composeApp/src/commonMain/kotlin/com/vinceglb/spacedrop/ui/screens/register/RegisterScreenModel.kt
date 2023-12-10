package com.vinceglb.spacedrop.ui.screens.register

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import com.vinceglb.spacedrop.data.repository.DeviceRepository
import kotlinx.coroutines.launch

class RegisterScreenModel(
    private val deviceRepository: DeviceRepository,
) : ScreenModel {
    var uiState by mutableStateOf(RegisterScreenUiState())
        private set

    fun registerDevice(deviceName: String) {
        uiState = uiState.copy(loading = true)

        screenModelScope.launch {
            deviceRepository.registerDevice(deviceName)
        }.invokeOnCompletion {
            uiState = uiState.copy(loading = false)
        }
    }
}

data class RegisterScreenUiState(
    val loading: Boolean = false,
    val error: String? = null,
)
