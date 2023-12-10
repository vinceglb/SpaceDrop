package com.vinceglb.spacedrop

import android.app.Application
import com.vinceglb.spacedrop.di.composeModule
import com.vinceglb.spacedrop.di.composePlatformModule
import com.vinceglb.spacedrop.di.startAppKoin
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class SpaceDropApp : Application() {
    override fun onCreate() {
        super.onCreate()

        // Koin
        startKoin {
            androidContext(this@SpaceDropApp)
            startAppKoin(listOf(composeModule, composePlatformModule))
        }
    }
}
