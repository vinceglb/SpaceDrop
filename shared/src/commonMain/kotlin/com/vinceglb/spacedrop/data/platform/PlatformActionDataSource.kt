package com.vinceglb.spacedrop.data.platform

expect class PlatformActionDataSource {
    suspend fun sendNotification()
}
