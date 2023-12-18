package com.vinceglb.spacedrop.data.firebase

import co.touchlab.kermit.Logger

class MessagingRemoteDataSourceFirebase : MessagingRemoteDataSource {
    override suspend fun getMessagingToken(): String? {
        return firebaseToken.also {
            Logger.i("MessagingRemoteDataSourceFirebase") { "FCM token: $it" }
        }
    }
}

// TODO: This is a hack to get around the fact that the Firebase SDK needs cocoapods to be installed
var firebaseToken: String? = null
