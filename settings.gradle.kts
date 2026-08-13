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
include(":app-shell")
include(":core-db-api")
include(":core-db-impl")
include(":core-di")
include(":core-ui-base")
include(":core-utils")
include(":home-impl")
include(":game-api")
include(":game-impl")
include(":leaderboard-api")
include(":leaderboard-impl")
include(":navigation-api")
include(":navigation-impl")
