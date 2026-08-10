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
        namespace = "com.alan.queensland.app.compose"
        compileSdk = libs.versions.android.compileSdk.get().toInt()
        minSdk = libs.versions.android.minSdk.get().toInt()
        compilerOptions {
            jvmTarget = JvmTarget.JVM_17
        }
        androidResources {
            enable = true
        }
    }

    listOf(
        iosArm64(),
        iosSimulatorArm64()
    ).forEach { iosTarget ->
        iosTarget.binaries.framework {
            baseName = "Queensland"
            isStatic = true
            freeCompilerArgs += listOf("-Xbinary=bundleId=com.alan.queensland")
        }
    }

    sourceSets {
        commonMain.dependencies {
            implementation(project(":core-db-impl"))
            implementation(project(":core-di"))
            implementation(project(":core-ui-base"))
            implementation(project(":core-utils"))
            implementation(project(":game-impl"))
            implementation(project(":home-impl"))
            implementation(project(":navigation-api"))
            implementation(project(":navigation-impl"))

            implementation(libs.compose.runtime)
            implementation(libs.compose.foundation)
            implementation(libs.compose.material3)
            implementation(libs.compose.ui)
            implementation(libs.compose.components.resources)
            implementation(libs.navigation.compose)
            implementation(libs.tatarka.inject.runtime)
        }
    }
}

dependencies {
    add("kspAndroid", libs.tatarka.inject.compiler)
    add("kspIosArm64", libs.tatarka.inject.compiler)
    add("kspIosSimulatorArm64", libs.tatarka.inject.compiler)
}
