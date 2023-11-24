package com.vinceglb.spacedrop.util

import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.gotrue.handleDeeplinks
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import platform.Foundation.NSURL

object SupabaseUtil : KoinComponent {
    fun handleDeeplinks(url: NSURL) {
        val supabase: SupabaseClient by inject()
        supabase.handleDeeplinks(url)
    }
}
