import com.android.build.gradle.internal.cxx.configure.gradleLocalProperties
import com.codingfeline.buildkonfig.compiler.FieldSpec.Type.STRING

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidLibrary)
    alias(libs.plugins.serialization)
    // alias(libs.plugins.skie)
    alias(libs.plugins.buildKonfig)
}

kotlin {
    listOf(
        iosX64(),
        iosArm64(),
        iosSimulatorArm64()
    ).forEach { iosTarget ->
        iosTarget.binaries.framework {
            baseName = "Shared"
            isStatic = true
        }
    }
    
    androidTarget {
        compilations.all {
            kotlinOptions {
                jvmTarget = "21"
            }
        }
    }
    
    jvm()
    jvmToolchain(21)
    
    sourceSets {
        commonMain.dependencies {
            // Kotlin
            implementation(libs.kotlinx.coroutines.core)

            // Skie
            // implementation(libs.skie.annotations)

            // Koin
            implementation(libs.koin.core)

            // Supabase
            api(libs.supabase.auth)
            implementation(libs.supabase.gotrue)
            api(libs.supabase.storage)      // TODO transform into implementation
            implementation(libs.supabase.realtime)
            implementation(libs.supabase.postgrest)

            // Multiplatform Settings
            implementation(libs.multiplatform.settings)
            implementation(libs.multiplatform.settings.coroutines)

            // Logger
            api(libs.kermit)
            implementation(libs.kermit.koin)
        }

        jvmMain.dependencies {
            // Kotlin Coroutines
            runtimeOnly(libs.kotlinx.coroutines.swing)

            // Ktor
            implementation(libs.ktor.client.cio)

            // Logger for debugging Supabase Auth
            implementation(libs.slf4j.simple)
        }

        appleMain.dependencies {
            // KMM ViewModel
            implementation(libs.kmm.viewmodel)

            // Ktor
            implementation(libs.ktor.client.darwin)

            // Fix for Koin
            // https://github.com/cashapp/sqldelight/issues/4357#issuecomment-1839905700
            implementation(libs.stately.common)
        }

        androidMain.dependencies {
            // Ktor
            implementation(libs.ktor.client.okhttp)

            // Logger for debugging Supabase Auth
            implementation(libs.slf4j.simple)
        }

        all {
            languageSettings {
                optIn("kotlinx.cinterop.ExperimentalForeignApi")
            }
        }
    }
}

android {
    namespace = "com.vinceglb.spacedrop.shared"
    compileSdk = libs.versions.android.compileSdk.get().toInt()
    defaultConfig {
        minSdk = libs.versions.android.minSdk.get().toInt()
    }
}

buildkonfig {
    packageName = "com.vinceglb.spacedrop.shared"
    objectName = "SupabaseKeyConfig"

    defaultConfigs {
        buildConfigField(
            STRING,
            "SupabaseUrl",
            gradleLocalProperties(project.rootDir).getProperty("SUPABASE_URL")
        )
        buildConfigField(
            STRING,
            "SupabaseKey",
            gradleLocalProperties(project.rootDir).getProperty("SUPABASE_KEY")
        )
    }
}
