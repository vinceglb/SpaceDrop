package com.vinceglb.spacedrop.ui.screens.home.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.vinceglb.spacedrop.model.Device
import com.vinceglb.spacedrop.ui.components.PlatformIcon
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import nl.jacobras.humanreadable.HumanReadable

@Composable
fun ManageDevices(
    devices: List<Device>,
    currentDevice: Device?,
    onRenameDevice: (String, String) -> Unit,
    onDeleteDevice: (String) -> Unit,
    onSendNotification: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    Surface(
        color = MaterialTheme.colorScheme.surface,
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
        shape = MaterialTheme.shapes.small,
        modifier = modifier,
    ) {
        Column {
            devices.forEachIndexed { index, device ->
                DeviceItem(
                    device = device,
                    isCurrent = device == currentDevice,
                    onRenameDevice = onRenameDevice,
                    onDeleteDevice = onDeleteDevice,
                    onSendNotification = onSendNotification,
                )

                if (index < devices.size - 1) {
                    HorizontalDivider()
                }
            }
        }
    }
}

@Composable
private fun DeviceItem(
    device: Device,
    isCurrent: Boolean,
    onRenameDevice: (String, String) -> Unit,
    onDeleteDevice: (String) -> Unit,
    onSendNotification: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    var isDropdownOpen by remember(device) { mutableStateOf(false) }
    var isRenameDeviceDialogOpen by remember(device) { mutableStateOf(false) }
    var newDeviceName by remember(device) { mutableStateOf(device.name) }

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .fillMaxWidth()
            .clickable { onSendNotification(device.id) }
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
                if (isCurrent) {
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

            val lastSeenStr = remember(device.lastSeen) {
                when (isCurrent) {
                    true -> ""
                    else -> "· ${HumanReadable.timeAgo(device.lastSeen)
                        .replaceFirstChar { if (it.isLowerCase()) it.titlecase() else it.toString() }}"
                }
            }

            Text(
                "${device.platform.name} $lastSeenStr",
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.outline,
            )
        }

        Spacer(modifier = Modifier.weight(1f))

        IconButton(
            onClick = { isDropdownOpen = true },
            modifier = Modifier.padding(16.dp)
        ) {
            Icon(
                Icons.Default.MoreVert,
                contentDescription = "More",
                tint = MaterialTheme.colorScheme.outline,
            )

            DropdownMenu(
                expanded = isDropdownOpen,
                onDismissRequest = { isDropdownOpen = false },
            ) {
                DropdownMenuItem(
                    text = { Text("Rename") },
                    onClick = {
                        isDropdownOpen = false
                        isRenameDeviceDialogOpen = true
                    },
                    leadingIcon = {
                        Icon(
                            Icons.Default.Edit,
                            contentDescription = null
                        )
                    }
                )
                DropdownMenuItem(
                    text = { Text("Delete") },
                    onClick = {
                        isDropdownOpen = false
                        onDeleteDevice(device.id)
                    },
                    leadingIcon = {
                        Icon(
                            Icons.Outlined.Delete,
                            contentDescription = null
                        )
                    }
                )
            }
        }
    }

    if (isRenameDeviceDialogOpen) {
        AlertDialog(
            onDismissRequest = { isRenameDeviceDialogOpen = false },
            title = { Text("Rename Device") },
            text = {
                Column {
                    Text(
                        "Enter a new name for your device",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurface,
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = newDeviceName,
                        onValueChange = { newDeviceName = it },
                        modifier = Modifier.fillMaxWidth(),
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        isRenameDeviceDialogOpen = false
                        onRenameDevice(device.id, newDeviceName)
                    }
                ) {
                    Text("Rename")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = { isRenameDeviceDialogOpen = false }
                ) {
                    Text("Cancel")
                }
            },
        )
    }
}

fun getTimeElapsedDescription(instant: Instant): String {
    val currentInstant = Clock.System.now()
    val duration = currentInstant - instant

    val minutes = duration.inWholeMinutes
    val hours = duration.inWholeHours
    val days = duration.inWholeDays

    return when {
        days > 0 -> "$days ${if (days > 1) "days" else "day"} ago"
        hours > 0 -> "$hours ${if (hours > 1) "hours" else "hour"} ago"
        minutes > 0 -> "$minutes ${if (minutes > 1) "minutes" else "minute"} ago"
        else -> "now"
    }
}
