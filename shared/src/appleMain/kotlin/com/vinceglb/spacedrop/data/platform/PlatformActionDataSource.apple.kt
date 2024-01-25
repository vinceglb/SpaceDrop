package com.vinceglb.spacedrop.data.platform

import platform.UIKit.UIAlertAction
import platform.UIKit.UIAlertActionStyleDefault
import platform.UIKit.UIAlertController
import platform.UIKit.UIAlertControllerStyleAlert
import platform.UIKit.UIApplication

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
}
