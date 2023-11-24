package com.vinceglb.spacedrop.data.supabase

import com.vinceglb.spacedrop.model.AuthUser
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.gotrue.SessionStatus
import io.github.jan.supabase.gotrue.auth
import io.github.jan.supabase.gotrue.providers.Google
import io.github.jan.supabase.gotrue.providers.builtin.IDToken
import io.github.jan.supabase.gotrue.user.UserInfo
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.shareIn
import kotlinx.serialization.json.jsonPrimitive

class AuthUserRemoteDataSource(
    private val client: SupabaseClient,
    applicationScope: CoroutineScope,
) {

    internal val authUser: SharedFlow<AuthUser?> = client.auth
        .sessionStatus
        .map(::processUserStatus)
        .shareIn(
            scope = applicationScope,
            replay = 1,
            started = SharingStarted.WhileSubscribed()
        )

    internal suspend fun loginWithGoogle(idToken: String? = null) {
        when (idToken) {
            null -> client.auth.signInWith(Google)
            else -> client.auth.signInWith(IDToken) {
                this.idToken = idToken
                provider = Google
            }
        }
    }

    internal suspend fun logout() {
        client.auth.signOut()
    }

    private fun processUserStatus(status: SessionStatus): AuthUser? {
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
