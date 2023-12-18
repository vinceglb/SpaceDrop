package com.vinceglb.spacedrop.data.firebase

import co.touchlab.kermit.Logger
import com.google.firebase.messaging.FirebaseMessaging
import kotlinx.coroutines.tasks.await

class MessagingRemoteDataSourceFirebase(
    private val messaging: FirebaseMessaging,
) : MessagingRemoteDataSource {
    override suspend fun getMessagingToken(): String? {
        return messaging.token.await().also { token ->
            Logger.i("MessagingRemoteDataSourceFirebase") { "FCM token: $token" }
        }
    }
}
