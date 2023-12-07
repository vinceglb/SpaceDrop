package com.vinceglb.spacedrop.ui.screens.login

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import cafe.adriel.voyager.core.model.ScreenModel
import io.github.jan.supabase.compose.auth.ComposeAuth

class LoginScreenModel(
    composeAuth: ComposeAuth,
) : ScreenModel {
    var uiState by mutableStateOf(LoginScreenUiState(composeAuth = composeAuth))
        private set

    fun startingSignIn() {
        uiState = uiState.copy(loading = true, error = null)
    }

    fun signInError(error: String) {
        uiState = uiState.copy(loading = false, error = error)
    }
}

data class LoginScreenUiState(
    val composeAuth: ComposeAuth,
    val loading: Boolean = false,
    val error: String? = null,
)
