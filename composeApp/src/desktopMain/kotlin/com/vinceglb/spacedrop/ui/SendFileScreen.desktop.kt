package com.vinceglb.spacedrop.ui

import androidx.compose.runtime.Composable
import com.darkrockstudios.libraries.mpfilepicker.FilePicker
import java.io.File

@Composable
actual fun PlatformFilePicker(
    show: Boolean,
    onFileSelected: (UploadFile) -> Unit,
) {
    FilePicker(
        show = show,
        onFileSelected = { mpFile ->
            mpFile?.let {
                val file = it.platformFile as File
                val uploadFile = UploadFile(file)
                onFileSelected(uploadFile)
            }
        }
    )
}
