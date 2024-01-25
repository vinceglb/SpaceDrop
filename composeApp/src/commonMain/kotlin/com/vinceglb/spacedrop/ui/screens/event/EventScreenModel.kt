package com.vinceglb.spacedrop.ui.screens.event

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import com.vinceglb.spacedrop.data.repository.EventRepository
import kotlinx.coroutines.launch

class EventScreenModel(
    private val eventId: String,
    private val eventRepository: EventRepository,
) : ScreenModel {
    var uiState: EventScreenUiState by mutableStateOf(EventScreenUiState(eventId))
        private set

    init {
        screenModelScope.launch {
            uiState = uiState.copy(isExecuting = true)
            eventRepository.executeEvent(eventId)
            uiState = uiState.copy(isExecuting = false)
        }
    }
    
    fun done() {
        screenModelScope.launch {
            eventRepository.removeNotificationEventId()
        }
    }
}

data class EventScreenUiState(
    val eventId: String,
    val isExecuting: Boolean = false,
)
