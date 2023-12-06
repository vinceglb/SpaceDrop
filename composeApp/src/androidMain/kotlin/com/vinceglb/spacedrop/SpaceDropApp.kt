package com.vinceglb.spacedrop

import android.app.Application
import com.vinceglb.spacedrop.di.androidModule
import com.vinceglb.spacedrop.di.composeModule
import com.vinceglb.spacedrop.di.initKoin

class SpaceDropApp : Application() {
    override fun onCreate() {
        super.onCreate()
        initKoin(listOf(composeModule, androidModule))
    }
}
