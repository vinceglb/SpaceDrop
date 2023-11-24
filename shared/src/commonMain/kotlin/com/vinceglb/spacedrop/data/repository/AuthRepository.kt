package com.vinceglb.spacedrop.data.repository

import com.vinceglb.spacedrop.data.supabase.AuthUserRemoteDataSource
import com.vinceglb.spacedrop.model.AuthUser
import kotlinx.coroutines.flow.SharedFlow

class AuthRepository(
    private val authUserRemoteDataSource: AuthUserRemoteDataSource,
) {
    val currentUser: SharedFlow<AuthUser?> =
        authUserRemoteDataSource.authUser

    suspend fun loginWithGoogle() {
        authUserRemoteDataSource.loginWithGoogle()
    }

    suspend fun loginWithIdToken(idToken: String) {
        authUserRemoteDataSource.loginWithGoogle(idToken)
    }
    
    suspend fun logout() {
        authUserRemoteDataSource.logout()
    }
}
