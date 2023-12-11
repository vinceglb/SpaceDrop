package com.vinceglb.spacedrop.data.supabase

import co.touchlab.kermit.Logger
import com.vinceglb.spacedrop.data.repository.AuthRepository
import com.vinceglb.spacedrop.model.Device
import com.vinceglb.spacedrop.model.DeviceCreateRequest
import io.github.jan.supabase.postgrest.Postgrest
import io.github.jan.supabase.realtime.PostgresAction
import io.github.jan.supabase.realtime.Realtime
import io.github.jan.supabase.realtime.channel
import io.github.jan.supabase.realtime.postgresChangeFlow
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.shareIn

interface DeviceRemoteDataSource {
    fun getDevices(): Flow<List<Device>>

    suspend fun createDevice(createDevice: DeviceCreateRequest): Device

    suspend fun updateDeviceFcmToken(deviceId: String, token: String): Device
}

class DeviceRemoteDataSourceSupabase(
    authRepository: AuthRepository,
    postgrest: Postgrest,
    realtime: Realtime,
    applicationScope: CoroutineScope,
) : DeviceRemoteDataSource {
    private val devicesTable = postgrest["devices"]
    private val devicesChannel = realtime.channel("devices")

    @Suppress("RemoveExplicitTypeArguments")
    @OptIn(ExperimentalCoroutinesApi::class)
    private val devices: SharedFlow<List<Device>> = authRepository
        .getCurrentUser()
        .flatMapLatest { user ->
            when (user) {
                null -> flow { emit(emptyList<Device>()) }
                else -> flow {
                    // Emit an initial value
                    emit(fetchDevices())

                    // Subscribe to changes
                    emitAll(
                        devicesChannel
                            .postgresChangeFlow<PostgresAction>(schema = "public") {
                                table = "devices"
                            }
                            .map { fetchDevices() }
                            .onStart { devicesChannel.subscribe() }
                            .onCompletion { devicesChannel.unsubscribe() }
                    )
                }
            }
        }
        .onEach { Logger.i("DeviceRemoteDataSourceSupabase") { "Devices: ${it.map { it.name }}" } }
        .shareIn(
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
