package com.vinceglb.spacedrop.di

import io.github.jan.supabase.gotrue.AuthConfig

actual fun AuthConfig.platformAuthConfig() {
    scheme = "com.vinceglb.spacedrop"
    host = "login"
}
