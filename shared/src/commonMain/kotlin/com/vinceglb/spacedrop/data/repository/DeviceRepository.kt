package com.vinceglb.spacedrop.data.repository

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
import kotlinx.coroutines.flow.shareIn

class DeviceRepository(
    applicationScope: CoroutineScope,
    private val deviceLocalDataSource: DeviceLocalDataSource,
    private val deviceRemoteDataSource: DeviceRemoteDataSource,
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
        // Create the device
        // TODO: Get the FCM token
        val device = deviceRemoteDataSource.createDevice(
            DeviceCreateRequest(
                name = deviceName,
                fcmToken = null,
                platform = currentPlatform,
            )
        )

        // Save the device ID locally
        deviceLocalDataSource.setDeviceId(device.id)
    }
}
