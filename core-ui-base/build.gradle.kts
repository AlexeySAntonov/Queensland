import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidMultiplatformLibrary)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
}

kotlin {
    android {
        namespace = "com.alan.queensland.core.ui.base"
        compileSdk = libs.versions.android.compileSdk.get().toInt()
        minSdk = libs.versions.android.minSdk.get().toInt()
        compilerOptions {
            jvmTarget = JvmTarget.JVM_17
        }
        androidResources {
            enable = true
        }
    }

    iosArm64()
    iosSimulatorArm64()

    sourceSets {
        commonMain.dependencies {
            implementation(project(":core-utils"))

            api(libs.androidx.lifecycle.viewmodelCompose)
            api(libs.androidx.lifecycle.viewmodel)
            api(libs.compose.foundation)
            api(libs.compose.materialIconsCore)
            api(libs.compose.material3)
            api(libs.compose.runtime)
            api(libs.compose.ui)
            implementation(libs.compose.components.resources)
        }
    }
}
