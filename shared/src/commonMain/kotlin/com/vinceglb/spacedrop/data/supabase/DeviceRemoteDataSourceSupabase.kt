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
import kotlinx.coroutines.delay
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

    suspend fun deleteDevice(deviceId: String)

    suspend fun renameDevice(deviceId: String, name: String)

    suspend fun updateDeviceFcmToken(deviceId: String, token: String): Device
}

class DeviceRemoteDataSourceSupabase(
    authRepository: AuthRepository,
    postgrest: Postgrest,
    realtime: Realtime,
    applicationScope: CoroutineScope,
) : DeviceRemoteDataSource {
    private val deviceTableName = "devices"
    private val devicesTable = postgrest[deviceTableName]
    private val devicesChannel = realtime.channel("devices-realtime")

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

                    // TODO improve this
                    // Wait 1 second to avoid conflicts EventRemoteDataSourceSupabase
                    delay(1_000)

                    // Subscribe to changes
                    emitAll(
                        devicesChannel
                            .postgresChangeFlow<PostgresAction>(schema = "public") { table = deviceTableName }
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

    override suspend fun deleteDevice(deviceId: String) {
        devicesTable
            .delete { filter { Device::id eq deviceId } }
    }

    override suspend fun renameDevice(deviceId: String, name: String) {
        devicesTable
            .update({ Device::name setTo name }) { filter { Device::id eq deviceId } }
    }

    override suspend fun updateDeviceFcmToken(deviceId: String, token: String): Device =
        devicesTable
            .update({ Device::fcmToken setTo token }) { filter { Device::id eq deviceId } }
            .decodeSingle<Device>()

    private suspend fun fetchDevices(): List<Device> =
        devicesTable
            .select()
            .decodeList()
}
