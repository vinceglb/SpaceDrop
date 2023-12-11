package com.vinceglb.spacedrop.data.settings

import co.touchlab.kermit.Logger
import com.russhwolf.settings.ExperimentalSettingsApi
import com.russhwolf.settings.coroutines.FlowSettings
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.shareIn

interface DeviceLocalDataSource {
    fun getDeviceId(): Flow<String?>

    suspend fun setDeviceId(deviceId: String?)
}

@OptIn(ExperimentalSettingsApi::class)
class DeviceLocalDataSourcePreferences(
    private val settings: FlowSettings,
    applicationScope: CoroutineScope,
) : DeviceLocalDataSource {
    private val deviceId: Flow<String?> = settings
        .getStringOrNullFlow(DEVICE_ID_KEY)
        .onEach { Logger.i("DeviceLocalDataSourcePreferences") { "Device ID: $it" } }
        .shareIn(
            scope = applicationScope,
            replay = 1,
            started = SharingStarted.WhileSubscribed()
        )

    override fun getDeviceId(): Flow<String?> =
        deviceId

    override suspend fun setDeviceId(deviceId: String?) {
        when (deviceId) {
            null -> settings.remove(DEVICE_ID_KEY)
            else -> settings.putString(DEVICE_ID_KEY, deviceId)
        }
    }

    private companion object {
        const val DEVICE_ID_KEY = "DEVICE_ID_KEY"
    }
}
