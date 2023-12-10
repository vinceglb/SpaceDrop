package com.vinceglb.spacedrop.util

import co.touchlab.kermit.Logger
import com.vinceglb.spacedrop.model.Platform

actual val currentPlatform: Platform
    get() {
        val operSys = System.getProperty("os.name").lowercase()
        return if (operSys.contains("win")) {
            Platform.Windows
        } else if (operSys.contains("nix") || operSys.contains("nux") ||
            operSys.contains("aix")
        ) {
            Platform.Linux
        } else if (operSys.contains("mac")) {
            Platform.MacOS
        } else {
            Logger.e("PlatformUtil.jvm") { "Unknown platform: $operSys" }
            Platform.Linux
        }
    }
