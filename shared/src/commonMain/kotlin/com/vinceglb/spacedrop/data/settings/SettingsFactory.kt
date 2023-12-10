package com.vinceglb.spacedrop.data.settings

import com.russhwolf.settings.ExperimentalSettingsApi
import com.russhwolf.settings.coroutines.FlowSettings

expect class SettingsFactory {
    @OptIn(ExperimentalSettingsApi::class)
    fun createSettings(): FlowSettings
}
