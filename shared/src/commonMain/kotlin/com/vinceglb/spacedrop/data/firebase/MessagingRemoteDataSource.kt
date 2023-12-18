package com.vinceglb.spacedrop.data.firebase

interface MessagingRemoteDataSource {
    suspend fun getMessagingToken(): String?
}
