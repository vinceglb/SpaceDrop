package com.vinceglb.spacedrop.data.supabase

import co.touchlab.kermit.Logger
import com.vinceglb.spacedrop.model.AuthUser
import io.github.jan.supabase.gotrue.Auth
import io.github.jan.supabase.gotrue.SessionStatus
import io.github.jan.supabase.gotrue.user.UserInfo
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.shareIn
import kotlinx.serialization.json.jsonPrimitive

class AuthUserRemoteDataSource(
    private val auth: Auth,
    applicationScope: CoroutineScope,
) {

    val authUser: SharedFlow<AuthUser?> = auth
        .sessionStatus
        .map(::processUserStatus)
        .shareIn(
            scope = applicationScope,
            replay = 1,
            started = SharingStarted.WhileSubscribed()
        )

    suspend fun signOut() {
        auth.signOut()
    }

    private fun processUserStatus(status: SessionStatus): AuthUser? {
        Logger.d("AuthUserRemoteDataSource") { "processUserStatus $status" }
        return when (status) {
            is SessionStatus.Authenticated -> {
                when (val user: UserInfo? = status.session.user) {
                    null -> null
                    else -> AuthUser(
                        id = user.id,
                        email = user.email,
                        name = user.userMetadata?.get("full_name")?.jsonPrimitive?.content,
                    )
                }
            }

            else -> null
        }
    }
}
