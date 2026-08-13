import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidMultiplatformLibrary)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
    alias(libs.plugins.ksp)
}

kotlin {
    android {
        namespace = "com.alan.queensland.leaderboard.impl"
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
            implementation(project(":core-db-api"))
            implementation(project(":core-di"))
            implementation(project(":core-ui-base"))
            implementation(project(":core-utils"))
            implementation(project(":navigation-api"))

            implementation(libs.compose.foundation)
            implementation(libs.compose.material3)
            implementation(libs.compose.runtime)
            implementation(libs.compose.ui)
            implementation(libs.kermit)
            implementation(libs.kotlinx.atomicfu)
            implementation(libs.tatarka.inject.runtime)
        }
    }
}

dependencies {
    add("kspAndroid", libs.tatarka.inject.compiler)
    add("kspIosArm64", libs.tatarka.inject.compiler)
    add("kspIosSimulatorArm64", libs.tatarka.inject.compiler)
}
