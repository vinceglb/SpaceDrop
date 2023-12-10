package com.vinceglb.spacedrop.ui.screens.home.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.vinceglb.spacedrop.model.Device
import com.vinceglb.spacedrop.ui.components.PlatformIcon

@Composable
fun ManageDevices(
    devices: List<Device>,
    currentDevice: Device?,
    modifier: Modifier = Modifier,
) {
    Surface(
        color = MaterialTheme.colorScheme.surface,
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
        shape = MaterialTheme.shapes.small,
        modifier = modifier,
    ) {
        LazyColumn {
            itemsIndexed(devices) { index, device ->
                DeviceItem(
                    device = device,
                    displayCurrent = device == currentDevice,
                )

                if (index < devices.size - 1) {
                    Divider()
                }
            }
        }
    }
}

@Composable
private fun DeviceItem(
    device: Device,
    displayCurrent: Boolean,
    modifier: Modifier = Modifier,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier.fillMaxWidth()
    ) {
        PlatformIcon(
            platform = device.platform,
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier.padding(20.dp),
        )

        Column {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    device.name,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface,
                )
                if (displayCurrent) {
                    Surface(
                        color = MaterialTheme.colorScheme.secondary.copy(alpha = 0.3f),
                        shape = CircleShape,
                        modifier = Modifier.padding(start = 8.dp)
                    ) {
                        Surface(
                            color = MaterialTheme.colorScheme.secondary,
                            shape = CircleShape,
                            content = {},
                            modifier = Modifier
                                .padding(3.dp)
                                .size(6.dp)
                        )
                    }
                }
            }
            Text(
                device.platform.name,
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.outline,
            )
        }
    }
}
