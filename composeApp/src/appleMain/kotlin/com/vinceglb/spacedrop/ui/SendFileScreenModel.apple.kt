package com.vinceglb.spacedrop.ui

import io.github.jan.supabase.storage.BucketApi

actual class UploadFile(
    val file: ByteArray,
    actual val filename: String,
)

actual suspend fun BucketApi.upload(
    path: String,
    uploadFile: UploadFile,
    upsert: Boolean,
) {
    upload(path, uploadFile.file, upsert)
}
