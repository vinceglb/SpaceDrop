package com.vinceglb.spacedrop.di

import com.vinceglb.spacedrop.data.firebase.MessagingRemoteDataSource
import com.vinceglb.spacedrop.data.firebase.MessagingRemoteDataSourceFirebase
import com.vinceglb.spacedrop.util.SendNotification
import org.koin.core.module.Module
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

actual val composePlatformModule: Module = module {
    // DataSources
    factoryOf(::MessagingRemoteDataSourceFirebase) { bind<MessagingRemoteDataSource>() }
}

fun desktopModule(onSendNotification: SendNotification): Module = module {
    factory { onSendNotification }
}
