package com.vinceglb.spacedrop.data.platform

expect class PlatformActionDataSource {
    suspend fun sendNotification()

    fun copyToClipboard(text: String)

    fun openUrl(url: String)
}
