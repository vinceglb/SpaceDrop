package com.vinceglb.spacedrop.data.supabase

import com.vinceglb.spacedrop.model.Secret
import com.vinceglb.spacedrop.model.SecretCreateRequest
import io.github.jan.supabase.postgrest.Postgrest

interface SecretRemoteDataSource {
    suspend fun fetchSecret(userId: String): Secret?

    suspend fun createSecret(createRequest: SecretCreateRequest): Secret
}

class SecretRemoteDataSourceSupabase(
    postgrest: Postgrest,
) : SecretRemoteDataSource {
    private val secretTableName = "secrets"
    private val secretTable = postgrest[secretTableName]

//    private val secret: SharedFlow<Secret?> = authRepository
//        .getCurrentUser()
//        .map { user -> user?.let { fetchSecret(it.id) } }
//        .onEach { Logger.i(TAG) { "secret: $it" } }
//        .shareIn(
//            scope = applicationScope,
//            started = kotlinx.coroutines.flow.SharingStarted.WhileSubscribed(),
//            replay = 1,
//        )
//
//    override fun getSecret(): Flow<Secret?> =
//        secret

    override suspend fun createSecret(createRequest: SecretCreateRequest): Secret =
        secretTable
            .insert(createRequest) { select() }
            .decodeSingle()

    override suspend fun fetchSecret(userId: String): Secret? =
        secretTable
            .select { filter { Secret::id eq userId } }
            .decodeSingleOrNull<Secret>()

//    companion object {
//        private const val TAG = "SecretRemoteDataSourceSupabase"
//    }
}
