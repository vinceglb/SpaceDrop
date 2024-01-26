package com.vinceglb.spacedrop.data.platform

import com.vinceglb.spacedrop.util.SendNotification
import java.awt.Desktop
import java.awt.Toolkit
import java.awt.datatransfer.StringSelection
import java.net.URI

actual class PlatformActionDataSource(
    private val onSendNotification: SendNotification,
) {
    actual suspend fun sendNotification() {
        onSendNotification("SpaceDrop", "This is a notification")
    }

    actual fun copyToClipboard(text: String) {
        val selection = StringSelection(text)
        val clipboard = Toolkit.getDefaultToolkit().systemClipboard
        clipboard.setContents(selection, selection)
    }

    actual fun openUrl(url: String) {
        // Runtime.getRuntime().exec("open $url")
        val desktop = Desktop.getDesktop()
        desktop.browse(URI.create(url))
    }
}
