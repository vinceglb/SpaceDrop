package com.vinceglb.spacedrop.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.getScreenModel
import com.vinceglb.spacedrop.data.repository.AuthRepository
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.compose.auth.composable.rememberSignInWithApple
import io.github.jan.supabase.compose.auth.composable.rememberSignInWithGoogle
import io.github.jan.supabase.compose.auth.composeAuth
import io.github.jan.supabase.gotrue.auth
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.painterResource
import org.koin.compose.koinInject

class HomeScreen : Screen {

    @Composable
    override fun Content() {
        val viewModel = getScreenModel<MainScreenModel>()
        val state by viewModel.state.collectAsState()

        Content(state)
    }

    @OptIn(ExperimentalResourceApi::class)
    @Composable
    private fun Content(state: String) {
        var greetingText by remember { mutableStateOf("Hello World!") }
        var showImage by remember { mutableStateOf(false) }
        Column(Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {

            Text(state)
            AuthVince()

            Button(onClick = {
                greetingText = "Salut"
                showImage = !showImage
            }) {
                Text(greetingText)
            }

            AnimatedVisibility(showImage) {
                Image(
                    painterResource("compose-multiplatform.xml"),
                    null
                )
            }
        }
    }

    @Composable
    private fun AuthVince() {
        val client = koinInject<SupabaseClient>()
        val authRepository = koinInject<AuthRepository>()
        val currentUser by authRepository.currentUser.collectAsState(initial = null)
        val coroutineScope = rememberCoroutineScope()
        val action = client.composeAuth.rememberSignInWithGoogle(onResult = {
            println("Google $it")
        })
//        val action2 = client.composeAuth.rememberSignInWithApple(onResult = {
//            println("Apple $it")
//        })

        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("Current user = ${currentUser?.email}")

            Crossfade(
                targetState = currentUser
            ) { user ->
                when (user) {
                    null -> Column {
                        Button(onClick = { action.startFlow() }) {
                            Text("Google Login")
                        }
//                        Button(onClick = { action2.startFlow() }) {
//                            Text("Apple login")
//                        }
                    }

                    else -> Button(onClick = { coroutineScope.launch { client.auth.signOut() } }) {
                        Text("Logout")
                    }
                }
            }
        }
    }

}