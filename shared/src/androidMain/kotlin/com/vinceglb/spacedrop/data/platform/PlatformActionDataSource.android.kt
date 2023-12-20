package com.vinceglb.spacedrop.data.platform

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
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
}