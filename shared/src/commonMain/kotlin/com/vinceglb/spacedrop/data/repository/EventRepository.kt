package com.vinceglb.spacedrop.data.repository

import com.vinceglb.spacedrop.data.settings.DeviceLocalDataSource
import com.vinceglb.spacedrop.data.supabase.EventRemoteDataSource
import com.vinceglb.spacedrop.model.Event
import com.vinceglb.spacedrop.model.EventCreateRequest
import com.vinceglb.spacedrop.model.EventType
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.shareIn

class EventRepository(
    applicationScope: CoroutineScope,
    private val deviceLocalDataSource: DeviceLocalDataSource,
    private val eventRemoteDataSource: EventRemoteDataSource,
) {
    private val deviceEvents: SharedFlow<List<Event>> =
        combine(
            deviceLocalDataSource.getDeviceId(),
            eventRemoteDataSource.getEvents(),
        ) { deviceId, events ->
            events.filter { it.destinationDeviceId == deviceId }
        }.shareIn(
            scope = applicationScope,
            started = SharingStarted.WhileSubscribed(),
            replay = 1,
        )

    fun getDeviceEvents(): Flow<List<Event>> =
        deviceEvents

    suspend fun sendNotificationEvent(destinationDeviceId: String) {
        val currentDeviceId = deviceLocalDataSource.getDeviceId().firstOrNull()
            ?: throw IllegalStateException("No device ID found")

        eventRemoteDataSource.createEvent(
            EventCreateRequest(
                type = EventType.NOTIFICATION,
                sourceDeviceId = currentDeviceId,
                destinationDeviceId = destinationDeviceId,
            )
        )
    }
}
