package com.vinceglb.spacedrop.di

import com.vinceglb.spacedrop.ui.MainScreenModel
import org.koin.dsl.module

val composeModule = module {
    factory { MainScreenModel() }
}
