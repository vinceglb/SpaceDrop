package com.vinceglb.spacedrop.model

import kotlinx.datetime.Instant
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Device(
    val id: String,

    val name: String,

    val platform: Platform,

    @SerialName("fcm_token")
    val fcmToken: String?,

    @SerialName("user_id")
    val userId: String,

    @SerialName("created_at")
    val createdAt: Instant,

    @SerialName("last_seen")
    val lastSeen: Instant,
)

@Serializable
data class DeviceCreateRequest(
    val name: String,

    val platform: Platform,

    @SerialName("fcm_token")
    val fcmToken: String?,
)
