package com.vinceglb.spacedrop.di

import co.touchlab.kermit.Logger
import co.touchlab.kermit.koin.KermitKoinLogger
import co.touchlab.skie.configuration.annotations.DefaultArgumentInterop
import com.vinceglb.spacedrop.data.repository.AuthRepository
import com.vinceglb.spacedrop.data.supabase.AuthUserRemoteDataSource
import com.vinceglb.spacedrop.shared.SupabaseKeyConfig
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.compose.auth.ComposeAuth
import io.github.jan.supabase.compose.auth.appleNativeLogin
import io.github.jan.supabase.compose.auth.composeAuth
import io.github.jan.supabase.compose.auth.googleNativeLogin
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.gotrue.Auth
import io.github.jan.supabase.gotrue.AuthConfig
import io.github.jan.supabase.gotrue.auth
import io.github.jan.supabase.storage.Storage
import io.github.jan.supabase.storage.storage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import org.koin.core.KoinApplication
import org.koin.core.context.startKoin
import org.koin.core.module.Module
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val commonModule = module {
    // Coroutine
    single { CoroutineScope(SupervisorJob() + Dispatchers.Default) }

    // Repositories
    singleOf(::AuthRepository)

    // DataSources
    factoryOf(::AuthUserRemoteDataSource)

    // Supabase
    single {
        createSupabaseClient(
            supabaseUrl = SupabaseKeyConfig.SupabaseUrl,
            supabaseKey = SupabaseKeyConfig.SupabaseKey,
        ) {
            install(Auth) {
                platformAuthConfig()
            }

            install(ComposeAuth) {
                appleNativeLogin()
                googleNativeLogin(serverClientId = "1072436209813-b9glgs6fsj6srj2g5lm23k48to59ft6p.apps.googleusercontent.com")
            }

            install(Storage)
        }
    }
    factory { get<SupabaseClient>().auth }
    factory { get<SupabaseClient>().storage }
    factory { get<SupabaseClient>().composeAuth }
}

expect fun AuthConfig.platformAuthConfig()

@DefaultArgumentInterop.Enabled
fun initKoin(modules: List<Module> = emptyList()) {
    startKoin {
        startAppKoin(modules)
    }
}

fun KoinApplication.startAppKoin(modules: List<Module>) {
    // Logger
    logger(KermitKoinLogger(Logger.withTag("Koin")))

    // Modules
    modules(
        commonModule,
        *modules.toTypedArray(),
    )
}
