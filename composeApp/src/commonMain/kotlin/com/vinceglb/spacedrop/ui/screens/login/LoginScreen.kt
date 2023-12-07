package com.vinceglb.spacedrop.ui.screens.login

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.getScreenModel
import io.github.jan.supabase.compose.auth.ComposeAuth
import io.github.jan.supabase.compose.auth.composable.NativeSignInResult
import io.github.jan.supabase.compose.auth.composable.rememberSignInWithGoogle

object LoginScreen : Screen {
    @Composable
    override fun Content() {
        val screenModel = getScreenModel<LoginScreenModel>()
        LoginScreen(
            uiState = screenModel.uiState,
            startingSignIn = screenModel::startingSignIn,
            signInError = screenModel::signInError,
        )
    }
}

@Composable
private fun LoginScreen(
    uiState: LoginScreenUiState,
    startingSignIn: () -> Unit,
    signInError: (String) -> Unit,
) {
    Scaffold {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.fillMaxSize(),
        ) {
            Card(
                modifier = Modifier
                    .widthIn(max = 512.dp)
                    .padding(16.dp),
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp)
                ) {
                    Icon(
                        Icons.Default.AccountCircle,
                        contentDescription = "Account Circle",
                        modifier = Modifier.size(56.dp),
                    )
                    Spacer(modifier = Modifier.size(16.dp))
                    Text(
                        "Login Screen",
                        style = MaterialTheme.typography.titleLarge,
                    )
                    Spacer(modifier = Modifier.size(4.dp))
                    Text(
                        "This is the login screen of SpaceDrop.",
                        style = MaterialTheme.typography.bodyMedium,
                    )
                    Spacer(modifier = Modifier.size(32.dp))
                    SignInWithGoogleButton(
                        composeAuth = uiState.composeAuth,
                        loading = uiState.loading,
                        startingSignIn = startingSignIn,
                        signInError = signInError
                    )
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
    }
}

@Composable
private fun SignInWithGoogleButton(
    composeAuth: ComposeAuth,
    loading: Boolean,
    startingSignIn: () -> Unit,
    signInError: (String) -> Unit,
) {
    val action = composeAuth.rememberSignInWithGoogle(onResult = { result ->
        when (result) {
            is NativeSignInResult.Error -> signInError(result.message)
            is NativeSignInResult.NetworkError -> signInError(result.message)
            else -> {}
        }
    })

    Button(
        enabled = !loading,
        onClick = {
            startingSignIn()
            action.startFlow()
        }
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
        ) {
            if (loading) {
                CircularProgressIndicator(modifier = Modifier.size(24.dp))
            } else {
                Icon(
                    Icons.Default.Person,
                    contentDescription = "Account Circle",
                    modifier = Modifier.size(24.dp),
                )
            }
            Spacer(modifier = Modifier.size(8.dp))
            Text("Sign in with Google")
        }
    }
}
