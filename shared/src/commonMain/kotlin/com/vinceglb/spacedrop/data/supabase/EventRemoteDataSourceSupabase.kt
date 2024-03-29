package com.vinceglb.spacedrop.data.supabase

import co.touchlab.kermit.Logger
import com.vinceglb.spacedrop.data.repository.AuthRepository
import com.vinceglb.spacedrop.model.Event
import com.vinceglb.spacedrop.model.EventCreateRequest
import io.github.jan.supabase.annotations.SupabaseExperimental
import io.github.jan.supabase.postgrest.Postgrest
import io.github.jan.supabase.realtime.Realtime
import io.github.jan.supabase.realtime.channel
import io.github.jan.supabase.realtime.postgresListDataFlow
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.shareIn

interface EventRemoteDataSource {
    fun getEvents(): Flow<List<Event>>

    suspend fun getEvent(eventId: String): Event?

    suspend fun createEvent(createEvent: EventCreateRequest): Event

    suspend fun deleteEvent(eventId: String)
}

class EventRemoteDataSourceSupabase(
    authRepository: AuthRepository,
    postgrest: Postgrest,
    realtime: Realtime,
    applicationScope: CoroutineScope,
) : EventRemoteDataSource {
    private val eventTableName = "events"
    private val eventsTable = postgrest[eventTableName]
    private val eventsChannel = realtime.channel("events-realtime")

    @Suppress("RemoveExplicitTypeArguments")
    @OptIn(ExperimentalCoroutinesApi::class, SupabaseExperimental::class)
    private val events: SharedFlow<List<Event>> = authRepository
        .getCurrentUser()
        .flatMapLatest { user ->
            when (user) {
                null -> flow { emit(emptyList<Event>()) }
                else -> eventsChannel.postgresListDataFlow(
                    table = eventTableName,
                    primaryKey = Event::id
                )
                    .onStart { eventsChannel.subscribe() }
                    .onCompletion { eventsChannel.unsubscribe() }
            }
        }
        .onEach { Logger.i(TAG) { "Events: ${it.map { it.id }}" } }
        .shareIn(
            scope = applicationScope,
            started = SharingStarted.WhileSubscribed(),
            replay = 1,
        )

    override fun getEvents(): Flow<List<Event>> =
        events

    override suspend fun getEvent(eventId: String): Event? =
        eventsTable
            .select { filter { Event::id eq eventId } }
            .decodeSingleOrNull()

    override suspend fun createEvent(createEvent: EventCreateRequest): Event =
        eventsTable
            .insert(createEvent) { select() }
            .decodeSingle()

    override suspend fun deleteEvent(eventId: String) {
        eventsTable
            .delete { filter { Event::id eq eventId } }
    }

    companion object {
        private const val TAG = "EventRemoteDataSourceSupabase"
    }
}
