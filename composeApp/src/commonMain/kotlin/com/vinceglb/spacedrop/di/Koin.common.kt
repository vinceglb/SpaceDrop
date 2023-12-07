package com.vinceglb.spacedrop.di

import com.vinceglb.spacedrop.ui.SendFileScreenModel
import com.vinceglb.spacedrop.ui.screens.MainScreenModel
import com.vinceglb.spacedrop.ui.screens.login.LoginScreenModel
import org.koin.core.module.Module
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

val composeModule = module {
    // ScreenModels
    factoryOf(::MainScreenModel)
    factoryOf(::LoginScreenModel)
    factoryOf(::SendFileScreenModel)
}

expect val composePlatformModule: Module
