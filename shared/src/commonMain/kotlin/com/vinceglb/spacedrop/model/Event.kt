package com.vinceglb.spacedrop.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Event(
    val id: String,

    @SerialName("user_id")
    val userId: String,

    val type: EventType,

    @SerialName("source_device_id")
    val sourceDeviceId: String,

    @SerialName("destination_device_id")
    val destinationDeviceId: String,
)

@Serializable
data class EventCreateRequest(
    val type: EventType,

    @SerialName("source_device_id")
    val sourceDeviceId: String,

    @SerialName("destination_device_id")
    val destinationDeviceId: String,
)

enum class EventType {
    NOTIFICATION,
}
