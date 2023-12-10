package com.vinceglb.spacedrop.ui.screens.register

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.getScreenModel
import com.vinceglb.spacedrop.ui.components.OnboardingHeader
import com.vinceglb.spacedrop.ui.components.OnboardingLayout
import com.vinceglb.spacedrop.ui.components.PlatformIcon
import com.vinceglb.spacedrop.util.currentPlatform

object RegisterScreen : Screen {
    @Composable
    override fun Content() {
        val screenModel = getScreenModel<RegisterScreenModel>()
        RegisterScreen(
            uiState = screenModel.uiState,
            onRegisterDevice = screenModel::registerDevice,
        )
    }
}

@Composable
private fun RegisterScreen(
    uiState: RegisterScreenUiState,
    onRegisterDevice: (String) -> Unit,
) {
    var deviceName by remember { mutableStateOf("") }

    OnboardingLayout {
        OnboardingHeader(
            icon = Icons.Default.AccountBox,
            iconDescription = "App Registration icon",
            title = "Register your device",
            subtitle = "This is the register screen of SpaceDrop.",
            modifier = Modifier.padding(bottom = 32.dp),
        )

        Column(Modifier.width(IntrinsicSize.Min)) {
            TextField(
                value = deviceName,
                onValueChange = { deviceName = it },
                label = { Text("Device Name") },
                trailingIcon = {
                    PlatformIcon(platform = currentPlatform)
                },
                modifier = Modifier.padding(bottom = 16.dp),
            )

            Button(
                enabled = !uiState.loading,
                onClick = { onRegisterDevice(deviceName) },
                modifier = Modifier.fillMaxWidth(),
            ) {
                if (uiState.loading) {
                    CircularProgressIndicator(modifier = Modifier.size(24.dp))
                    Spacer(modifier = Modifier.width(12.dp))
                }
                Text("Register")
            }

            if (uiState.error != null) {
                Spacer(modifier = Modifier.size(2.dp))
                Text(
                    uiState.error,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.error,
                    textAlign = TextAlign.Center,
                )
            }
        }
    }
}
