package com.vinceglb.spacedrop.ui.screens.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.getScreenModel
import com.vinceglb.spacedrop.ui.screens.home.components.ManageDevices

object HomeScreen : Screen {
    @Composable
    override fun Content() {
        val screenModel = getScreenModel<HomeScreenModel>()
        val uiState by screenModel.uiState.collectAsState()
        HomeScreen(
            uiState = uiState,
            onSignOut = screenModel::signOut,
            onRenameDevice = screenModel::renameDevice,
            onDeleteDevice = screenModel::deleteDevice,
            onSendNotification = screenModel::sendNotificationEvent,
            onConsumeMessage = screenModel::consumeMessage,
        )
    }
}

@Composable
private fun HomeScreen(
    uiState: HomeScreenUiState,
    onSignOut: () -> Unit,
    onRenameDevice: (String, String) -> Unit,
    onDeleteDevice: (String) -> Unit,
    onSendNotification: (String, String) -> Unit,
    onConsumeMessage: () -> Unit,
) {
    val scrollState = rememberScrollState()
    val snackbarHostState = remember { SnackbarHostState() }
    var text by remember { mutableStateOf("") }

    LaunchedEffect(uiState.message) {
        if (uiState.message != null) {
            snackbarHostState.showSnackbar(uiState.message)
            onConsumeMessage()
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.fillMaxSize(),
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .widthIn(max = 512.dp)
                    .verticalScroll(scrollState)
                    .padding(16.dp),
            ) {
                Surface(
                    color = MaterialTheme.colorScheme.primary.copy(0.2f),
                    shape = CircleShape,
                ) {
                    Icon(
                        Icons.Default.Person,
                        contentDescription = "Home",
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier
                            .padding(16.dp)
                            .size(44.dp)
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(2.dp)
                ) {
                    Text(
                        text = "Manage your devices",
                        style = MaterialTheme.typography.titleLarge,
                        textAlign = TextAlign.Center,
                    )
                    Text(
                        text = "You can manage your devices here.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.outline,
                        textAlign = TextAlign.Center,
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                TextField(
                    value = text,
                    onValueChange = { text = it },
                )

                Spacer(modifier = Modifier.height(16.dp))

                ManageDevices(
                    devices = uiState.devices,
                    currentDevice = uiState.currentDevice,
                    onRenameDevice = onRenameDevice,
                    onDeleteDevice = onDeleteDevice,
                    onSendNotification = { deviceId ->
                        onSendNotification(deviceId, text)
                    },
                )

                Spacer(modifier = Modifier.height(16.dp))

                Button(onClick = onSignOut) {
                    Text("Sign out")
                }
            }
        }
    }
}
