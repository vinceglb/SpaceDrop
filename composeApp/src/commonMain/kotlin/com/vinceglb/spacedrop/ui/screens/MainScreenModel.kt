package com.vinceglb.spacedrop.ui.screens

import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import com.vinceglb.spacedrop.data.repository.AuthRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn

class MainScreenModel(
    authRepository: AuthRepository,
) : ScreenModel {
    val uiState: StateFlow<MainState> = combine(
        authRepository.currentUser,
        authRepository.currentUser,     // TODO replace with isRegistered
    ) { isLogged, _ ->
        MainState.Success(
            isLogged = isLogged != null,
            isRegistered = false,       // TODO replace with isRegistered
        )
    }.stateIn(
        scope = screenModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = MainState.Loading,
    )

    override fun onDispose() {
        super.onDispose()
        println("MainScreenModel onDispose")
    }
}

sealed class MainState {
    data object Loading : MainState()
    data class Success(
        val isLogged: Boolean,
        val isRegistered: Boolean,
    ) : MainState()
}
