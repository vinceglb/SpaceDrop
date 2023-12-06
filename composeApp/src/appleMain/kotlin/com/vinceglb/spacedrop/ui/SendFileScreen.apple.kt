package com.vinceglb.spacedrop.ui

import androidx.compose.runtime.Composable
import com.darkrockstudios.libraries.mpfilepicker.FilePicker
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.readBytes
import platform.Foundation.NSData
import platform.Foundation.NSURL
import platform.Foundation.dataWithContentsOfURL

@OptIn(ExperimentalForeignApi::class)
@Composable
actual fun PlatformFilePicker(
    show: Boolean,
    onFileSelected: (UploadFile) -> Unit
) {
    FilePicker(
        show = show,
        onFileSelected = { mpFile ->
            mpFile?.let {
                // Get the url
                val nsUrl = it.platformFile as NSURL

                // Get the bytes
                val bytes = NSData.dataWithContentsOfURL(nsUrl)?.let { nsData ->
                    nsData.bytes?.readBytes(nsData.length.toInt())
                } ?: byteArrayOf()

                // Create the upload file
                val uploadFile = UploadFile(
                    file = bytes,
                    filename = "ios.${nsUrl.scheme}"
                )

                // Call the callback
                onFileSelected(uploadFile)
            }
        }
    )
}
