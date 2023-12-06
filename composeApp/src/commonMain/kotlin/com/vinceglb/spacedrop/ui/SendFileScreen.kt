package com.vinceglb.spacedrop.ui

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.inset
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.getScreenModel

class SendFileScreen : Screen {
    @Composable
    override fun Content() {
        val screenModel = getScreenModel<SendFileScreenModel>()
        SendFile(
            loading = screenModel.loading,
            onFileSelected = screenModel::sendFile,
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SendFile(
    loading: Boolean,
    onFileSelected: (UploadFile) -> Unit,
) {
    var showFilePicker by remember { mutableStateOf(false) }

    Column {
        AuthVince()

        Text(text = "Send file")

        val primaryColor = MaterialTheme.colorScheme.primary
        val cornerRadius = 32f

        Card(
            onClick = { showFilePicker = true },
            shape = RoundedCornerShape(cornerRadius),
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .padding(16.dp)
        ) {
            Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
                if (loading) {
                    CircularProgressIndicator()
                } else {
                    Text(
                        text = "Drop file here",
                        style = MaterialTheme.typography.labelLarge,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }

                DottedLine(color = primaryColor, cornerRadius = cornerRadius)
            }
        }
    }

    PlatformFilePicker(
        show = showFilePicker,
        onFileSelected = {
            showFilePicker = false
            onFileSelected(it)
        },
    )
}

@Composable
private fun DottedLine(color: Color, cornerRadius: Float) {
    Canvas(modifier = Modifier.fillMaxSize()) {
        // Dotted line border
        // Inset is 2f because the stroke is 4f wide
        inset(inset = 2f) {
            drawRoundRect(
                color = color,
                cornerRadius = CornerRadius(cornerRadius),
                style = Stroke(
                    width = 4f,
                    pathEffect = PathEffect.dashPathEffect(floatArrayOf(16f, 12f), 0f),
                    cap = StrokeCap.Round
                ),
            )
        }
    }
}

@Composable
expect fun PlatformFilePicker(
    show: Boolean,
    onFileSelected: (UploadFile) -> Unit
)
