package com.vinceglb.spacedrop.di

import co.touchlab.skie.configuration.annotations.DefaultArgumentInterop
import org.koin.core.context.startKoin
import org.koin.core.module.Module
import org.koin.dsl.module

val commonModule = module {
        
}

@DefaultArgumentInterop.Enabled
fun initKoin(modules: List<Module> = emptyList()) {
    startKoin {
        modules(
            commonModule,
            *modules.toTypedArray(),
        )
    }
}
