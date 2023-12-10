package com.vinceglb.spacedrop.data.repository

import com.vinceglb.spacedrop.data.supabase.AuthUserRemoteDataSource
import com.vinceglb.spacedrop.model.AuthUser
import kotlinx.coroutines.flow.Flow

class AuthRepository(
    private val authUserRemoteDataSource: AuthUserRemoteDataSource,
) {
    fun getCurrentUser(): Flow<AuthUser?> =
        authUserRemoteDataSource.getCurrentUser()
    
    suspend fun signOut() {
        authUserRemoteDataSource.signOut()
    }
}
