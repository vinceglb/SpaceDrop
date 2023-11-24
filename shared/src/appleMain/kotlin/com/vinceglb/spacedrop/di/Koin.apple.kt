package com.vinceglb.spacedrop.di

import io.github.jan.supabase.gotrue.AuthConfig

actual fun AuthConfig.platformAuthConfig() {
    host = "com.vinceglb.spacedrop"
    scheme = "login"
}
