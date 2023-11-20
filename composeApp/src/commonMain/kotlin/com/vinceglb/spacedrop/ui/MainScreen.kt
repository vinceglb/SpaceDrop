package com.vinceglb.spacedrop.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.getScreenModel

class HomeScreen : Screen {

    @Composable
    override fun Content() {
        val viewModel = getScreenModel<MainScreenModel>()
        val state by viewModel.state.collectAsState()
        
        Content(state)
    }
    
    @Composable
    private fun Content(state: String) {
        var greetingText by remember { mutableStateOf("Hello World!") }
        var showImage by remember { mutableStateOf(false) }
        Column(Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
            
            Text(state)
            
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

}