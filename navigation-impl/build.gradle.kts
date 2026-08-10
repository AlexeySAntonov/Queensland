import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidMultiplatformLibrary)
    alias(libs.plugins.ksp)
}

kotlin {
    android {
        namespace = "com.alan.queensland.navigation.impl"
        compileSdk = libs.versions.android.compileSdk.get().toInt()
        minSdk = libs.versions.android.minSdk.get().toInt()
        compilerOptions {
            jvmTarget = JvmTarget.JVM_17
        }
    }

    iosArm64()
    iosSimulatorArm64()

    sourceSets {
        commonMain.dependencies {
            api(project(":navigation-api"))
            implementation(project(":core-di"))

            implementation(libs.kotlinx.coroutines.core)
            implementation(libs.tatarka.inject.runtime)
        }
    }
}

dependencies {
    add("kspAndroid", libs.tatarka.inject.compiler)
    add("kspIosArm64", libs.tatarka.inject.compiler)
    add("kspIosSimulatorArm64", libs.tatarka.inject.compiler)
}
