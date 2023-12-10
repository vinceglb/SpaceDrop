package com.vinceglb.spacedrop.data.supabase

import com.vinceglb.spacedrop.model.Device
import com.vinceglb.spacedrop.model.DeviceCreateRequest
import io.github.jan.supabase.postgrest.Postgrest
import io.github.jan.supabase.realtime.PostgresAction
import io.github.jan.supabase.realtime.Realtime
import io.github.jan.supabase.realtime.channel
import io.github.jan.supabase.realtime.postgresChangeFlow
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.shareIn

interface DeviceRemoteDataSource {
    fun getDevices(): Flow<List<Device>>

    suspend fun createDevice(createDevice: DeviceCreateRequest): Device

    suspend fun updateDeviceFcmToken(deviceId: String, token: String): Device
}

class DeviceRemoteDataSourceSupabase(
    postgrest: Postgrest,
    realtime: Realtime,
    applicationScope: CoroutineScope,
) : DeviceRemoteDataSource {
    private val devicesTable = postgrest["devices"]
    private val devicesChannel = realtime.channel("devices")

    private val devices: SharedFlow<List<Device>> =
        flow {
            // Emit an initial value
            emit(fetchDevices())

            // Subscribe to changes
            emitAll(
                devicesChannel
                    .postgresChangeFlow<PostgresAction>(schema = "public") { table = "devices" }
                    .map { fetchDevices() }
                    .onStart { devicesChannel.subscribe() }
                    .onCompletion { devicesChannel.unsubscribe() }
            )
        }.shareIn(
            scope = applicationScope,
            replay = 1,
            started = SharingStarted.WhileSubscribed()
        )

    override fun getDevices(): Flow<List<Device>> =
        devices

    override suspend fun createDevice(createDevice: DeviceCreateRequest): Device =
        devicesTable
            .insert(createDevice) { select() }
            .decodeSingle<Device>()

    override suspend fun updateDeviceFcmToken(deviceId: String, token: String): Device =
        devicesTable
            .update({ Device::fcmToken setTo token }) { filter { Device::id eq deviceId } }
            .decodeSingle<Device>()

    private suspend fun fetchDevices(): List<Device> =
        devicesTable
            .select()
            .decodeList()
}
