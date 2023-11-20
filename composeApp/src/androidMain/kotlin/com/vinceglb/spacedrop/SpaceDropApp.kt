package com.vinceglb.spacedrop

import android.app.Application
import com.vinceglb.spacedrop.di.composeModule
import com.vinceglb.spacedrop.di.initKoin
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class SpaceDropApp : Application() {
    override fun onCreate() {
        super.onCreate()
        initKoin(listOf(composeModule))
    }
}
