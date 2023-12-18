package com.vinceglb.spacedrop.di

import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.ktx.messaging
import com.vinceglb.spacedrop.data.firebase.MessagingRemoteDataSource
import com.vinceglb.spacedrop.data.firebase.MessagingRemoteDataSourceFirebase
import org.koin.core.module.Module
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

actual val composePlatformModule: Module = module {
    // Firebase
    single { Firebase.messaging }

    // DataSources
    factoryOf(::MessagingRemoteDataSourceFirebase) { bind<MessagingRemoteDataSource>() }
}
