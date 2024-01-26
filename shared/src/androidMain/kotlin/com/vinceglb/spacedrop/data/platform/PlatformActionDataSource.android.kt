package com.vinceglb.spacedrop.data.platform

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat.getSystemService
import co.touchlab.kermit.Logger

actual class PlatformActionDataSource(
    private val context: Context,
) {
    actual suspend fun sendNotification() {
        Logger.i { "Sending notification" }

        val channelId = "CHANNEL_ID"

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            val permission = Manifest.permission.POST_NOTIFICATIONS
            val res = ActivityCompat.checkSelfPermission(context, permission)
            if (res != PackageManager.PERMISSION_GRANTED) {
                Logger.e { "No permission to send notifications" }
                return
            }
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                "SpaceDrop channel",
                NotificationManager.IMPORTANCE_DEFAULT
            ).apply {
                description = "SpaceDrop channel description"
            }
            // Register the channel with the system.
            val notificationManager: NotificationManager =
                getSystemService(context, NotificationManager::class.java)
                    ?: throw Exception("Notification manager is null")
            notificationManager.createNotificationChannel(channel)
        }

        val builder = NotificationCompat.Builder(context, channelId)
            .setSmallIcon(android.R.drawable.ic_dialog_info)
            .setContentTitle("SpaceDrop")
            .setContentText("This is a notification")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)

        with(NotificationManagerCompat.from(context)) {
            notify(0, builder.build())
        }
    }

    actual fun copyToClipboard(text: String) {
        // Copy to clipboard
        val clipboardManager = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clip = ClipData.newPlainText("Copied from Teleport", text)
        clipboardManager.setPrimaryClip(clip)

        // Show Toast
        Toast.makeText(context, "Copied to clipboard", Toast.LENGTH_SHORT).show()
    }

    actual fun openUrl(url: String) {
        // Open url
        val uri = Uri.parse(url)
        val intent = Intent(Intent.ACTION_VIEW, uri).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK
        }
        context.startActivity(intent)
    }
}
