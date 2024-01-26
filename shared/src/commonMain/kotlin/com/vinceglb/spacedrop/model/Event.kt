package com.vinceglb.spacedrop.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
sealed interface Event {
    val id: String
    val userId: String
    val sourceDeviceId: String
    val destinationDeviceId: String
}

@Serializable
@SerialName("ping")
data class PingEvent(
    @SerialName("id")
    override val id: String,

    @SerialName("user_id")
    override val userId: String,

    @SerialName("source_device_id")
    override val sourceDeviceId: String,

    @SerialName("destination_device_id")
    override val destinationDeviceId: String,
) : Event

@Serializable
@SerialName("text")
data class TextEvent(
    @SerialName("id")
    override val id: String,

    @SerialName("user_id")
    override val userId: String,

    @SerialName("source_device_id")
    override val sourceDeviceId: String,

    @SerialName("destination_device_id")
    override val destinationDeviceId: String,

    @SerialName("payload")
    val text: String,
) : Event

@Serializable
@SerialName("url")
data class UrlEvent(
    @SerialName("id")
    override val id: String,

    @SerialName("user_id")
    override val userId: String,

    @SerialName("source_device_id")
    override val sourceDeviceId: String,

    @SerialName("destination_device_id")
    override val destinationDeviceId: String,

    @SerialName("payload")
    val url: String,
) : Event

@Serializable
sealed interface EventCreateRequest {
    val sourceDeviceId: String
    val destinationDeviceId: String
}

@Serializable
@SerialName("ping")
data class PingEventCreateRequest(
    @SerialName("source_device_id")
    override val sourceDeviceId: String,

    @SerialName("destination_device_id")
    override val destinationDeviceId: String,
) : EventCreateRequest

@Serializable
@SerialName("text")
data class TextEventCreateRequest(
    @SerialName("source_device_id")
    override val sourceDeviceId: String,

    @SerialName("destination_device_id")
    override val destinationDeviceId: String,

    @SerialName("payload")
    val text: String,
) : EventCreateRequest

@Serializable
@SerialName("url")
data class UrlEventCreateRequest(
    @SerialName("source_device_id")
    override val sourceDeviceId: String,

    @SerialName("destination_device_id")
    override val destinationDeviceId: String,

    @SerialName("payload")
    val url: String,
) : EventCreateRequest
