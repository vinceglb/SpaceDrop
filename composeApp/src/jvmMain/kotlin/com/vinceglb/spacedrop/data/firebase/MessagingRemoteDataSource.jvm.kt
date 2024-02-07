package com.vinceglb.spacedrop.data.firebase

class MessagingRemoteDataSourceFirebase : MessagingRemoteDataSource {
    override suspend fun getMessagingToken(): String? {
        return null
    }
}
