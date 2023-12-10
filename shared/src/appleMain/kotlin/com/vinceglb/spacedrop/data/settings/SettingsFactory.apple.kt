package com.vinceglb.spacedrop.data.settings

import com.russhwolf.settings.ExperimentalSettingsApi
import com.russhwolf.settings.NSUserDefaultsSettings
import com.russhwolf.settings.coroutines.FlowSettings
import com.russhwolf.settings.coroutines.toFlowSettings
import platform.Foundation.NSUserDefaults

actual class SettingsFactory {
    @OptIn(ExperimentalSettingsApi::class)
    actual fun createSettings(): FlowSettings {
        val delegate = NSUserDefaults.standardUserDefaults
        return NSUserDefaultsSettings(delegate).toFlowSettings()
    }
}
