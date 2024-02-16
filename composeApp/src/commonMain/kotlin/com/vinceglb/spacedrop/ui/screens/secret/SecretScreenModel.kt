package com.vinceglb.spacedrop.ui.screens.secret

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import co.touchlab.kermit.Logger
import com.vinceglb.spacedrop.data.repository.SecretRepository
import com.vinceglb.spacedrop.model.Secret
import kotlinx.coroutines.launch

class SecretScreenModel(
    private val secretRepository: SecretRepository,
) : ScreenModel {
    var uiState by mutableStateOf<SecretScreenUiState>(SecretScreenUiState.Loading)
        private set

    init {
        screenModelScope.launch {
            val secret = secretRepository.fetchSecret()
            uiState = when (secret) {
                null -> SecretScreenUiState.FirstInitialization
                else -> SecretScreenUiState.DecryptSecret(secret)
            }
        }
    }

    fun createSecret(password: String) {
        screenModelScope.launch {
            secretRepository.createSecret(password)
        }
    }

    fun decryptSecret(password: String) {
        val uiState = uiState
        if (uiState !is SecretScreenUiState.DecryptSecret) return

        screenModelScope.launch {
            if (!secretRepository.checkPassword(password, uiState.secret)) {
                Logger.w { "Invalid password" }
                return@launch
            }

            secretRepository.decryptSecret(password, uiState.secret)
        }
    }
}

sealed class SecretScreenUiState {
    data object Loading : SecretScreenUiState()
    data object FirstInitialization : SecretScreenUiState()
    data class DecryptSecret(val secret: Secret) : SecretScreenUiState()
}
