package com.vinceglb.spacedrop.data.repository

import co.touchlab.kermit.Logger
import com.vinceglb.spacedrop.data.firebase.MessagingRemoteDataSource
import com.vinceglb.spacedrop.data.settings.DeviceLocalDataSource
import com.vinceglb.spacedrop.data.supabase.DeviceRemoteDataSource
import com.vinceglb.spacedrop.model.Device
import com.vinceglb.spacedrop.model.DeviceCreateRequest
import com.vinceglb.spacedrop.util.currentPlatform
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.shareIn

class DeviceRepository(
    applicationScope: CoroutineScope,
    private val deviceLocalDataSource: DeviceLocalDataSource,
    private val deviceRemoteDataSource: DeviceRemoteDataSource,
    private val messagingRemoteDataSource: MessagingRemoteDataSource,
) {
    private val currentDevice: SharedFlow<Device?> =
        combine(
            deviceLocalDataSource.getDeviceId(),
            deviceRemoteDataSource.getDevices(),
        ) { deviceId, devices ->
            devices.find { it.id == deviceId }
        }.shareIn(
            scope = applicationScope,
            started = SharingStarted.WhileSubscribed(),
            replay = 1,
        )

    fun getDevices(): Flow<List<Device>> =
        deviceRemoteDataSource.getDevices()

    fun getCurrentDevice(): Flow<Device?> =
        currentDevice

    suspend fun registerDevice(deviceName: String) {
        // Get the FCM token if any
        val fcmToken = messagingRemoteDataSource.getMessagingToken()

        // Create the device
        val device = deviceRemoteDataSource.createDevice(
            DeviceCreateRequest(
                name = deviceName,
                fcmToken = fcmToken,
                platform = currentPlatform,
            )
        )

        // Save the device ID locally
        deviceLocalDataSource.setDeviceId(device.id)
    }

    suspend fun updateFcmToken(fcmToken: String) {
        // Get the current device
        val currentDevice = currentDevice.firstOrNull()
            ?: throw IllegalStateException("No device ID found")

        if (currentDevice.fcmToken == fcmToken) {
            Logger.i("DeviceRepository") { "FCM token already up to date: $fcmToken" }
            return
        }

        // Update the FCM token
        deviceRemoteDataSource.updateDeviceFcmToken(currentDevice.id, fcmToken)
    }

    suspend fun deleteDevice(deviceId: String) {
        deviceRemoteDataSource.deleteDevice(deviceId)
    }

    suspend fun renameDevice(deviceId: String, name: String) {
        deviceRemoteDataSource.renameDevice(deviceId, name)
    }
}
