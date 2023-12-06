package com.vinceglb.spacedrop.di

import com.vinceglb.spacedrop.ui.MainScreenModel
import com.vinceglb.spacedrop.ui.SendFileScreenModel
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

val composeModule = module {
    factory { MainScreenModel() }
    factoryOf(::SendFileScreenModel)
}
