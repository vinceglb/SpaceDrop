package com.vinceglb.spacedrop.data.settings

import com.russhwolf.settings.ExperimentalSettingsApi
import com.russhwolf.settings.coroutines.FlowSettings

interface EventLocalDataSource {
    suspend fun isHandlingEvent(eventId: String): Boolean

    suspend fun addHandlingEvent(eventId: String)

    suspend fun removeHandlingEvent(eventId: String)
}

@OptIn(ExperimentalSettingsApi::class)
class EventLocalDataSourcePreferences(
    private val settings: FlowSettings,
) : EventLocalDataSource {
    override suspend fun isHandlingEvent(eventId: String): Boolean =
        settings.getStringOrNull(eventId) != null

    override suspend fun addHandlingEvent(eventId: String) {
        settings.putString(eventId, eventId)
    }

    override suspend fun removeHandlingEvent(eventId: String) {
        settings.remove(eventId)
    }
}
