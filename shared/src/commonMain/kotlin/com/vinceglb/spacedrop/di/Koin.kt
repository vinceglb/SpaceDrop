package com.vinceglb.spacedrop.di

import co.touchlab.kermit.Logger
import co.touchlab.kermit.koin.KermitKoinLogger
import com.russhwolf.settings.ExperimentalSettingsApi
import com.vinceglb.spacedrop.data.repository.AuthRepository
import com.vinceglb.spacedrop.data.repository.DeviceRepository
import com.vinceglb.spacedrop.data.repository.EventRepository
import com.vinceglb.spacedrop.data.settings.DeviceLocalDataSource
import com.vinceglb.spacedrop.data.settings.DeviceLocalDataSourcePreferences
import com.vinceglb.spacedrop.data.settings.EventLocalDataSource
import com.vinceglb.spacedrop.data.settings.EventLocalDataSourcePreferences
import com.vinceglb.spacedrop.data.settings.SettingsFactory
import com.vinceglb.spacedrop.data.supabase.AuthUserRemoteDataSource
import com.vinceglb.spacedrop.data.supabase.DeviceRemoteDataSource
import com.vinceglb.spacedrop.data.supabase.DeviceRemoteDataSourceSupabase
import com.vinceglb.spacedrop.data.supabase.EventRemoteDataSource
import com.vinceglb.spacedrop.data.supabase.EventRemoteDataSourceSupabase
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
import io.github.jan.supabase.postgrest.Postgrest
import io.github.jan.supabase.postgrest.postgrest
import io.github.jan.supabase.realtime.Realtime
import io.github.jan.supabase.realtime.realtime
import io.github.jan.supabase.serializer.KotlinXSerializer
import io.github.jan.supabase.storage.Storage
import io.github.jan.supabase.storage.storage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.serialization.json.Json
import org.koin.core.KoinApplication
import org.koin.core.context.startKoin
import org.koin.core.module.Module
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

@OptIn(ExperimentalSettingsApi::class)
val commonModule = module {
    // Coroutine
    single { CoroutineScope(SupervisorJob() + Dispatchers.Default) }

    // Multiplatform settings
    single {
        val factory = get<SettingsFactory>()
        factory.createSettings()
    }

    // Repositories
    singleOf(::AuthRepository)
    singleOf(::DeviceRepository)
    singleOf(::EventRepository)

    // DataSources
    factoryOf(::AuthUserRemoteDataSource)
    factoryOf(::DeviceLocalDataSourcePreferences) { bind<DeviceLocalDataSource>() }
    factoryOf(::EventLocalDataSourcePreferences) { bind<EventLocalDataSource>() }
    factoryOf(::DeviceRemoteDataSourceSupabase) { bind<DeviceRemoteDataSource>() }
    factoryOf(::EventRemoteDataSourceSupabase) { bind<EventRemoteDataSource>() }

    // Supabase
    single {
        createSupabaseClient(
            supabaseUrl = SupabaseKeyConfig.SupabaseUrl,
            supabaseKey = SupabaseKeyConfig.SupabaseKey,
        ) {
            defaultSerializer = KotlinXSerializer(
                Json {
                    ignoreUnknownKeys = true
                }
            )

            install(Auth) {
                platformAuthConfig()
            }

            install(ComposeAuth) {
                appleNativeLogin()
                googleNativeLogin(serverClientId = "1072436209813-b9glgs6fsj6srj2g5lm23k48to59ft6p.apps.googleusercontent.com")
            }

            install(Storage)
            install(Realtime)
            install(Postgrest)
        }
    }
    factory { get<SupabaseClient>().auth }
    factory { get<SupabaseClient>().storage }
    factory { get<SupabaseClient>().realtime }
    factory { get<SupabaseClient>().postgrest }
    factory { get<SupabaseClient>().composeAuth }
}

expect fun AuthConfig.platformAuthConfig()

expect val platformModule: Module

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
        platformModule,
        *modules.toTypedArray(),
    )
}
