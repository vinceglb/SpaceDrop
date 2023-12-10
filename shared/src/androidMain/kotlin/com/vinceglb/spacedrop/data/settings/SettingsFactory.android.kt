package com.vinceglb.spacedrop.data.settings

import android.content.Context
import android.content.SharedPreferences
import com.russhwolf.settings.ExperimentalSettingsApi
import com.russhwolf.settings.SharedPreferencesSettings
import com.russhwolf.settings.coroutines.FlowSettings
import com.russhwolf.settings.coroutines.toFlowSettings

actual class SettingsFactory(private val context: Context) {
    @OptIn(ExperimentalSettingsApi::class)
    actual fun createSettings(): FlowSettings {
        val delegate: SharedPreferences = context.getSharedPreferences("settings", Context.MODE_PRIVATE)
        return SharedPreferencesSettings(delegate).toFlowSettings()
    }
}
