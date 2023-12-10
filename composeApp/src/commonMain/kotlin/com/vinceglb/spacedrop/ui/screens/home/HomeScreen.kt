package com.vinceglb.spacedrop.ui.screens.home

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.getScreenModel

object HomeScreen : Screen {
    @Composable
    override fun Content() {
        val screenModel = getScreenModel<HomeScreenModel>()
        val uiState by screenModel.uiState.collectAsState()
        HomeScreen(uiState = uiState)
    }
}

@Composable
private fun HomeScreen(
    uiState: HomeScreenUiState,
) {
    Scaffold {
        Column {
            Text("Current user = ${uiState.currentUser?.name}")
            Text("Current device = ${uiState.currentDevice?.name}")

            Spacer(modifier = Modifier.height(16.dp))

            uiState.devices.forEach { device ->
                Text(device.name)
            }
        }
    }
}
