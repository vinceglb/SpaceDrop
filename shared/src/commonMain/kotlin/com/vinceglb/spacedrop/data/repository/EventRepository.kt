package com.vinceglb.spacedrop.data.repository

import co.touchlab.kermit.Logger
import com.vinceglb.spacedrop.data.platform.PlatformActionDataSource
import com.vinceglb.spacedrop.data.settings.DeviceLocalDataSource
import com.vinceglb.spacedrop.data.settings.EventLocalDataSource
import com.vinceglb.spacedrop.data.supabase.EventRemoteDataSource
import com.vinceglb.spacedrop.model.Event
import com.vinceglb.spacedrop.model.EventCreateRequest
import com.vinceglb.spacedrop.model.EventType
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.shareIn

class EventRepository(
    applicationScope: CoroutineScope,
    private val deviceLocalDataSource: DeviceLocalDataSource,
    private val eventLocalDataSource: EventLocalDataSource,
    private val eventRemoteDataSource: EventRemoteDataSource,
    private val platformActionDataSource: PlatformActionDataSource,
) {
    private val deviceEvents: SharedFlow<List<Event>> =
        combine(
            deviceLocalDataSource.getDeviceId(),
            eventRemoteDataSource.getEvents(),
        ) { deviceId, events ->
            events.filter { it.destinationDeviceId == deviceId }
        }
            .onEach { Logger.i(TAG) { "Device events: ${it.map { it.id }}" } }
            .shareIn(
                scope = applicationScope,
                started = SharingStarted.Eagerly,
                replay = 1,
            )

    private val notificationEventId: MutableStateFlow<String?> = MutableStateFlow(null)

    fun getDeviceEvents(): Flow<List<Event>> =
        deviceEvents

    fun getNotificationEventId(): Flow<String?> =
        notificationEventId

    suspend fun executeEvent(eventId: String) {
        eventRemoteDataSource.getEvent(eventId)?.let { event ->
            Logger.i(TAG) { "Execute event = $event" }
            executeEvent(event)
        }
    }

    suspend fun executeEvents() {
        val deviceEvents = deviceEvents.firstOrNull()
        Logger.i(TAG) { "executeEvents() ${deviceEvents?.size}" }
        deviceEvents?.forEach { event -> executeEvent(event) }
    }

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

    suspend fun onNotificationEventId(eventId: String) {
        notificationEventId.emit(eventId)
    }

    suspend fun removeNotificationEventId() {
        notificationEventId.emit(null)
    }

    private suspend fun executeEvent(event: Event) {
        // Check if event is already being handled
        if (eventLocalDataSource.isHandlingEvent(event.id)) {
            Logger.w(TAG) { "Event ${event.id} is already being handled" }
            return
        }

        try {
            // Add event to handling list
            eventLocalDataSource.addHandlingEvent(event.id)

            Logger.i(TAG) { "Executing event ${event.id}" }

            // Execute event
            when (event.type) {
                EventType.NOTIFICATION -> platformActionDataSource.sendNotification()
            }

            // Delete event remotely
            eventRemoteDataSource.deleteEvent(event.id)
        } catch (e: Exception) {
            Logger.e(TAG) { "Error executing event ${event.id}: $e" }
        }
    }

    companion object {
        private const val TAG = "EventRepository"
    }
}
