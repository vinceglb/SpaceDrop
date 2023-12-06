package com.vinceglb.spacedrop.ui

import io.github.jan.supabase.storage.BucketApi
import io.github.jan.supabase.storage.upload
import java.io.File

actual class UploadFile(val file: File) {
    actual val filename: String = file.name
}

actual suspend fun BucketApi.upload(
    path: String,
    uploadFile: UploadFile,
    upsert: Boolean,
) {
    upload(path, uploadFile.file, upsert)
}
