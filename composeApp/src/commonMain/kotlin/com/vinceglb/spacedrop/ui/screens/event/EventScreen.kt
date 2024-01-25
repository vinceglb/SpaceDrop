package com.vinceglb.spacedrop.ui.screens.event

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.getScreenModel
import org.koin.core.parameter.parametersOf

data class EventScreen(val eventId: String) : Screen {
    @Composable
    override fun Content() {
        val screenModel = getScreenModel<EventScreenModel> { parametersOf(eventId) }
        val uiState = screenModel.uiState

        EventScreen(
            uiState = uiState,
            onDone = screenModel::done,
        )
    }
}

@Composable
private fun EventScreen(
    uiState: EventScreenUiState,
    onDone: () -> Unit,
) {
    Scaffold {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.fillMaxSize()
        ) {
            Text("Event: ${uiState.eventId}")
            Button(onClick = onDone, enabled = !uiState.isExecuting) {
                if (uiState.isExecuting) {
                    CircularProgressIndicator(modifier = Modifier.size(24.dp))
                }

                Text("Done")
            }
        }
    }
}
