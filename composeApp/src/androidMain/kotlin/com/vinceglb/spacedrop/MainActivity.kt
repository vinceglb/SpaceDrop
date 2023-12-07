package com.vinceglb.spacedrop

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.vinceglb.spacedrop.di.composeModule
import com.vinceglb.spacedrop.di.composePlatformModule
import com.vinceglb.spacedrop.di.startAppKoin
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.gotrue.handleDeeplinks
import org.koin.android.ext.android.inject
import org.koin.compose.KoinApplication

class MainActivity : ComponentActivity() {
    private val supabaseClient: SupabaseClient by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        supabaseClient.handleDeeplinks(intent)

        setContent {
            KoinApplication(
                application = { startAppKoin(listOf(composeModule, composePlatformModule)) }
            ) {
                App()
            }
        }
    }
}

@Preview
@Composable
fun AppAndroidPreview() {
    App()
}