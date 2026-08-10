rootProject.name = "Queensland"

pluginManagement {
    repositories {
        google {
            mavenContent {
                includeGroupAndSubgroups("androidx")
                includeGroupAndSubgroups("com.android")
                includeGroupAndSubgroups("com.google")
            }
        }
        mavenCentral()
        gradlePluginPortal()
    }
}

dependencyResolutionManagement {
    repositories {
        google {
            mavenContent {
                includeGroupAndSubgroups("androidx")
                includeGroupAndSubgroups("com.android")
                includeGroupAndSubgroups("com.google")
            }
        }
        mavenCentral()
    }
}

include(":androidApp")
include(":app-compose")
include(":core-db-impl")
include(":core-di")
include(":core-ui-base")
include(":core-utils")
include(":game-api")
include(":game-impl")
include(":home-impl")
include(":navigation-api")
include(":navigation-impl")
