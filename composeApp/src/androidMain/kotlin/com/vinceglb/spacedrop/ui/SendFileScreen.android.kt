package com.vinceglb.spacedrop.ui

import android.net.Uri
import androidx.compose.runtime.Composable
import com.darkrockstudios.libraries.mpfilepicker.FilePicker

@Composable
actual fun PlatformFilePicker(
    show: Boolean,
    onFileSelected: (UploadFile) -> Unit
) {
    FilePicker(
        show = show,
        fileExtensions = listOf("jpg", "png"),
        onFileSelected = { mpFile ->
            mpFile?.let {
                val file = it.platformFile as Uri
                val uploadFile = UploadFile(file)
                onFileSelected(uploadFile)
            }
        }
    )
}
