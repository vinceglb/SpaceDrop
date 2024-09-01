package com.vinceglb.spacedrop.ui.components

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.vinceglb.spacedrop.ui.theme.SpaceDropTheme

@Composable
fun OnboardingHeader(
    icon: ImageVector,
    iconDescription: String,
    title: String,
    subtitle: String,
    modifier: Modifier = Modifier,
) {
    Column(
        horizontalAlignment = androidx.compose.ui.Alignment.CenterHorizontally,
        modifier = modifier
    ) {
        Icon(
            icon,
            contentDescription = iconDescription,
            modifier = Modifier.size(56.dp),
        )
        Spacer(modifier = Modifier.size(16.dp))
        Text(
            title,
            style = MaterialTheme.typography.titleLarge,
            textAlign = TextAlign.Center,
        )
        Spacer(modifier = Modifier.size(4.dp))
        Text(
            subtitle,
            style = MaterialTheme.typography.bodyMedium,
            textAlign = TextAlign.Center,
        )
    }
}

@Preview
@Composable
fun OnboardingHeaderPreview() {
    SpaceDropTheme {
        Surface {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center,
            ) {
                OnboardingHeader(
                    icon = Icons.Rounded.Check,
                    iconDescription = "Check",
                    title = "My Super very long title",
                    subtitle = "This is a subtitle with few words to explain things"
                )
            }
        }
    }
}
