package com.vinceglb.spacedrop.ui

import android.net.Uri
import io.github.jan.supabase.storage.BucketApi
import io.github.jan.supabase.storage.upload

actual class UploadFile(val uri: Uri) {
    actual val filename: String = uri.lastPathSegment ?: ""
}

actual suspend fun BucketApi.upload(
    path: String,
    uploadFile: UploadFile,
    upsert: Boolean,
) {
    upload(path, uploadFile.uri, upsert)
}
