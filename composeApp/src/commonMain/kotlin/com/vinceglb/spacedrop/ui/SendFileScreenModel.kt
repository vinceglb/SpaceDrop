package com.vinceglb.spacedrop.ui

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import com.vinceglb.spacedrop.data.repository.AuthRepository
import io.github.jan.supabase.storage.BucketApi
import io.github.jan.supabase.storage.Storage
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class SendFileScreenModel(
    private val storage: Storage,
    private val authRepository: AuthRepository,
) : ScreenModel {
    var loading by mutableStateOf(false)
        private set

    fun sendFile(uploadFile: UploadFile) {
        loading = true

        screenModelScope.launch {
            val user = authRepository.getCurrentUser().first()
            val userId = user?.id ?: return@launch

            val supabasePath = "$userId/${uploadFile.filename}"
            storage["drop"].upload(
                path = supabasePath,
                uploadFile = uploadFile,
                upsert = true,
            )
        }.invokeOnCompletion { loading = false }
    }

    fun signOut() {
        screenModelScope.launch {
            authRepository.signOut()
        }
    }
}

expect class UploadFile {
   val filename: String
}

expect suspend fun BucketApi.upload(
    path: String,
    uploadFile: UploadFile,
    upsert: Boolean = false,
)
