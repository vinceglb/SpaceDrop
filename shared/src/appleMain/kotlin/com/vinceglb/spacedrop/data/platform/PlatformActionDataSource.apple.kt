package com.vinceglb.spacedrop.data.platform

import platform.Foundation.NSURL
import platform.UIKit.UIAlertAction
import platform.UIKit.UIAlertActionStyleDefault
import platform.UIKit.UIAlertController
import platform.UIKit.UIAlertControllerStyleAlert
import platform.UIKit.UIApplication
import platform.UIKit.UIPasteboard

actual class PlatformActionDataSource {
    actual suspend fun sendNotification() {
        val alertController = UIAlertController.alertControllerWithTitle(
            title = "Hello",
            message = "World",
            preferredStyle = UIAlertControllerStyleAlert
        )

        val okAction = UIAlertAction.actionWithTitle(
            title = "OK",
            style = UIAlertActionStyleDefault,
            handler = null
        )

        alertController.addAction(okAction)

        UIApplication.sharedApplication.keyWindow?.rootViewController?.presentViewController(
            alertController,
            animated = true,
            completion = null
        )
    }

    actual fun copyToClipboard(text: String) {
        val pasteboard = UIPasteboard.generalPasteboard
        pasteboard.string = text
    }

    actual fun openUrl(url: String) {
        NSURL.URLWithString(url)?.let {
            UIApplication.sharedApplication.openURL(it)
        }
    }
}
