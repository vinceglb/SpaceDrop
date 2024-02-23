plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.jetbrainsCompose)
    alias(libs.plugins.googleServices)
    alias(libs.plugins.conveyor)
}

group = "com.vinceglb.spacedrop"
version = "0.2.1"

kotlin {
    androidTarget {
        compilations.all {
            kotlinOptions {
                jvmTarget = "17"
            }
        }
    }

    jvm(/*"desktop"*/)
    jvmToolchain(17)

    listOf(
        iosX64(),
        iosArm64(),
        iosSimulatorArm64()
    ).forEach { iosTarget ->
        iosTarget.binaries.framework {
            baseName = "ComposeApp"
            isStatic = true
        }
    }

    sourceSets {
        // val desktopMain by getting

        commonMain.dependencies {
            implementation(projects.shared)

            // Compose
            implementation(compose.runtime)
            implementation(compose.foundation)
            implementation(compose.material3)
            implementation(compose.ui)
            implementation(compose.components.resources)

            // Koin
            implementation(libs.koin.compose)

            // Voyager
            implementation(libs.voyager.navigator)
            implementation(libs.voyager.koin)

            // File picker
            implementation(libs.mpFilePicker)

            // Human readable duration
            implementation(libs.humanReadable)
        }

        androidMain.dependencies {
            // AndroidX
            implementation(libs.androidx.core)
            implementation(libs.androidx.activity.compose)

            // Compose
            implementation(libs.compose.ui)
            implementation(libs.compose.ui.tooling.preview)

            // Koin
            implementation(libs.koin.androidx.compose)

            // Accompanist
            implementation(libs.accompanist.permissions)

            // Firebase
            implementation(project.dependencies.platform(libs.firebase.bom))
            implementation(libs.firebase.analytics.ktx)
            implementation(libs.firebase.messaging.ktx)
        }

        jvmMain.dependencies {
            implementation(compose.desktop.currentOs)
        }

        targets.all {
            compilations.all {
                compilerOptions.configure {
                    freeCompilerArgs.add("-Xexpect-actual-classes")
                }
            }
        }
    }
}

android {
    namespace = "com.vinceglb.spacedrop"
    compileSdk = libs.versions.android.compileSdk.get().toInt()

//    sourceSets["main"].manifest.srcFile("src/androidMain/AndroidManifest.xml")
//    sourceSets["main"].res.srcDirs("src/androidMain/res")
//    sourceSets["main"].resources.srcDirs("src/commonMain/resources")

    defaultConfig {
        applicationId = "com.vinceglb.spacedrop"
        minSdk = libs.versions.android.minSdk.get().toInt()
        targetSdk = libs.versions.android.targetSdk.get().toInt()
        versionCode = 3
        versionName = "0.2.1"
    }

    buildFeatures {
        compose = true
    }

    composeOptions {
        kotlinCompilerExtensionVersion = libs.versions.compose.compiler.get()
    }

    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    dependencies {
        debugImplementation(libs.compose.ui.tooling)
    }
}

compose.desktop {
    application {
        mainClass = "com.vinceglb.spacedrop.MainKt"

//        nativeDistributions {
//            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
//            packageName = "SpaceDrop"
//            packageVersion = "1.0.0"
//
//            macOS {
//                iconFile.set(rootProject.file("icons/AppIcon.icns"))
//                infoPlist {
//                    extraKeysRawXml = """
//                        <key>LSUIElement</key>
//                        <true/>
//                    """.trimIndent()
//                }
//            }
//
//            windows {
//                iconFile.set(rootProject.file("icons/AppIcon.ico"))
//            }
//        }
    }
}

// region Conveyor Gradle config
// https://conveyor.hydraulic.dev/13.1/tutorial/tortoise/2-gradle/#adapting-a-compose-desktop-app

dependencies {
    linuxAmd64(compose.desktop.linux_x64)
    macAmd64(compose.desktop.macos_x64)
    macAarch64(compose.desktop.macos_arm64)
    windowsAmd64(compose.desktop.windows_x64)
}

// Work around temporary Compose bugs.
configurations.all {
    attributes {
        attribute(Attribute.of("ui", String::class.java), "awt")
    }
}
// endregion
