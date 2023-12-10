package com.vinceglb.spacedrop.ui.screens.login

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.getScreenModel
import com.vinceglb.spacedrop.ui.components.OnboardingHeader
import com.vinceglb.spacedrop.ui.components.OnboardingLayout
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
    OnboardingLayout {
        OnboardingHeader(
            icon = Icons.Default.AccountCircle,
            iconDescription = "Account Circle",
            title = "Login Screen",
            subtitle = "This is the login screen of SpaceDrop.",
            modifier = Modifier.padding(bottom = 32.dp)
        )

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
