package com.vinceglb.spacedrop.ui.screens.secret

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.getScreenModel
import com.vinceglb.spacedrop.ui.components.OnboardingHeader
import com.vinceglb.spacedrop.ui.components.OnboardingLayout
import com.vinceglb.spacedrop.ui.screens.secret.SecretScreenUiState.DecryptSecret
import com.vinceglb.spacedrop.ui.screens.secret.SecretScreenUiState.FirstInitialization

object SecretScreen : Screen {
    @Composable
    override fun Content() {
        val screenModel = getScreenModel<SecretScreenModel>()
        val uiState = screenModel.uiState

        var password by remember { mutableStateOf("") }

        OnboardingLayout {
            OnboardingHeader(
                icon = Icons.Default.Lock,
                iconDescription = "Lock",
                title = "Secret Screen",
                subtitle = when (uiState) {
                    is FirstInitialization -> "This is the first time you open SpaceDrop. Please create a secret password."
                    is DecryptSecret -> "Please enter your secret password to decrypt your secret."
                    else -> ""
                },
                modifier = Modifier.padding(bottom = 32.dp)
            )

            TextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Password") },
                visualTransformation = PasswordVisualTransformation(),
                modifier = Modifier.padding(bottom = 16.dp)
            )

            Button(
                onClick = {
                    when (uiState) {
                        is FirstInitialization -> screenModel.createSecret(password)
                        is DecryptSecret -> screenModel.decryptSecret(password)
                        else -> {}
                    }
                },
                enabled = password.isNotBlank()
            ) {
                Text("Save Secret")
            }
        }
    }
}


