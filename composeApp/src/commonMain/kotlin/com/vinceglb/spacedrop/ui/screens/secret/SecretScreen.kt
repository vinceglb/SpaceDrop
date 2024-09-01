package com.vinceglb.spacedrop.ui.screens.secret

import androidx.compose.desktop.ui.tooling.preview.Preview
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
import com.vinceglb.spacedrop.model.Secret
import com.vinceglb.spacedrop.ui.components.OnboardingHeader
import com.vinceglb.spacedrop.ui.components.OnboardingLayout
import com.vinceglb.spacedrop.ui.theme.SpaceDropTheme

object SecretScreen : Screen {
    @Composable
    override fun Content() {
        val screenModel = getScreenModel<SecretScreenModel>()
        val uiState = screenModel.uiState

        SecretScreen(
            uiState = uiState,
            createSecret = screenModel::createSecret,
            decryptSecret = screenModel::decryptSecret,
        )
    }
}

@Composable
private fun SecretScreen(
    uiState: SecretScreenUiState,
    createSecret: (String) -> Unit,
    decryptSecret: (String) -> Unit,
) {
    var password by remember { mutableStateOf("") }

    OnboardingLayout {
        OnboardingHeader(
            icon = Icons.Default.Lock,
            iconDescription = "Lock",
            title = "Secret Screen",
            subtitle = when (uiState) {
                is SecretScreenUiState.FirstInitialization -> "This is the first time you open SpaceDrop. Please create a secret password."
                is SecretScreenUiState.DecryptSecret -> "Please enter your secret password to decrypt your secret."
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
                    is SecretScreenUiState.FirstInitialization -> createSecret(password)
                    is SecretScreenUiState.DecryptSecret -> decryptSecret(password)
                    else -> {}
                }
            },
            enabled = password.isNotBlank()
        ) {
            Text("Save Secret")
        }
    }
}

@Preview
@Composable
private fun SecretScreenPreview() {
    SpaceDropTheme {
        SecretScreen(
            uiState = SecretScreenUiState.FirstInitialization,
            createSecret = {},
            decryptSecret = {},
        )
    }
}

@Preview
@Composable
private fun SecretScreenDecryptPreview() {
    SpaceDropTheme {
        SecretScreen(
            uiState = SecretScreenUiState.DecryptSecret(
                Secret(
                    id = "1",
                    passwordHash = "123",
                    publicKey = "public",
                    secretKeyEncrypted = "encrypted",
                    salt = "salt",
                    secretKeyNonce = "nonce",
                )
            ),
            createSecret = {},
            decryptSecret = {},
        )
    }
}
