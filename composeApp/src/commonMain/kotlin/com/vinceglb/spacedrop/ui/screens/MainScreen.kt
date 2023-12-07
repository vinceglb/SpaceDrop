package com.vinceglb.spacedrop.ui.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.getScreenModel
import cafe.adriel.voyager.navigator.CurrentScreen
import cafe.adriel.voyager.navigator.Navigator
import com.vinceglb.spacedrop.ui.SendFileScreen
import com.vinceglb.spacedrop.ui.screens.login.LoginScreen

object MainScreen : Screen {
    @Composable
    override fun Content() {
        val screenModel = getScreenModel<MainScreenModel>()
        val uiState by screenModel.uiState.collectAsState()

        Navigator(LoadingScreen) { navigator ->
            // Redirections
            LaunchedEffect(uiState) {
                val state = uiState
                if (state is MainState.Success) {
                    when  {
                        // Redirect to LoginScreen if not logged
                        !state.isLogged -> navigator.replaceAll(LoginScreen)

                        // Redirect to RegisterScreen if logged and not registered
                        !state.isRegistered -> navigator.replaceAll(SendFileScreen) // TODO replace with RegisterScreen

                        // Redirect to SendFileScreen if logged and registered
                        else -> navigator.replaceAll(SendFileScreen)
                    }
                }
            }

            CurrentScreen()
        }
    }
}

private object LoadingScreen : Screen {
    @Composable
    override fun Content() {
        Scaffold {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier.fillMaxSize()
            ) {
                CircularProgressIndicator()
            }
        }
    }
}
