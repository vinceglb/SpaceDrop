package com.vinceglb.spacedrop.data.supabase

import com.vinceglb.spacedrop.data.repository.AuthRepository
import com.vinceglb.spacedrop.model.Event
import com.vinceglb.spacedrop.model.EventCreateRequest
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
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.shareIn

interface EventRemoteDataSource {
    fun getEvents(): Flow<List<Event>>

    suspend fun createEvent(createEvent: EventCreateRequest): Event

    suspend fun deleteEvent(eventId: String)
}

class EventRemoteDataSourceSupabase(
    authRepository: AuthRepository,
    postgrest: Postgrest,
    realtime: Realtime,
    applicationScope: CoroutineScope,
) : EventRemoteDataSource {
    private val eventsTable = postgrest["events"]
    private val eventsChannel = realtime.channel("events")

    @Suppress("RemoveExplicitTypeArguments")
    @OptIn(ExperimentalCoroutinesApi::class)
    private val events: SharedFlow<List<Event>> = authRepository
        .getCurrentUser()
        .flatMapLatest { user ->
            when (user) {
                null -> flow { emit(emptyList<Event>()) }
                else -> flow {
                    // Emit an initial value
                    emit(fetchEvents())

                    // Subscribe to changes
                    emitAll(
                        eventsChannel
                            .postgresChangeFlow<PostgresAction>(schema = "public") {
                                table = "events"
                            }
                            .map { fetchEvents() }
                            .onStart { eventsChannel.subscribe() }
                            .onCompletion { eventsChannel.unsubscribe() }
                    )
                }
            }
        }
        .shareIn(
            scope = applicationScope,
            started = SharingStarted.WhileSubscribed(),
            replay = 1,
        )

    override fun getEvents(): Flow<List<Event>> =
        events

    override suspend fun createEvent(createEvent: EventCreateRequest): Event =
        eventsTable
            .insert(createEvent) { select() }
            .decodeSingle()

    override suspend fun deleteEvent(eventId: String) {
        eventsTable
            .delete { filter { Event::id eq eventId } }
    }

    private suspend fun fetchEvents(): List<Event> =
        eventsTable
            .select()
            .decodeList()
}
