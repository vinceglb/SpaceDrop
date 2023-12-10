package com.vinceglb.spacedrop.di

import com.vinceglb.spacedrop.data.settings.SettingsFactory
import io.github.jan.supabase.gotrue.AuthConfig
import org.koin.core.module.Module
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

actual fun AuthConfig.platformAuthConfig() {
    host = "com.vinceglb.spacedrop"
    scheme = "login"
}

actual fun platformModule(): Module = module {
    factoryOf(::SettingsFactory)
}
