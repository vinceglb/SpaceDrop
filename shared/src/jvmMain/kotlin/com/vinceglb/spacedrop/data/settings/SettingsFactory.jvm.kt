package com.vinceglb.spacedrop.data.settings

import com.russhwolf.settings.ExperimentalSettingsApi
import com.russhwolf.settings.PreferencesSettings
import com.russhwolf.settings.coroutines.FlowSettings
import com.russhwolf.settings.coroutines.toFlowSettings
import java.util.prefs.Preferences

actual class SettingsFactory {
    @OptIn(ExperimentalSettingsApi::class)
    actual fun createSettings(): FlowSettings {
        val delegate = Preferences.userRoot()
        return PreferencesSettings(delegate).toFlowSettings()
    }
}
