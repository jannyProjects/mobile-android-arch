rootProject.buildFileName = "build.gradle.kts"

pluginManagement {
    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        mavenCentral()
        gradlePluginPortal()
        maven { url = java.net.URI("https://jitpack.io") }
    }
}

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

rootProject.name = "mobile_app_android"
include(":app")

include(":domain")
project(":domain").projectDir = file("./modules/domain")

include(":data")
project(":data").projectDir = file("./modules/data")

include(":common")
project(":common").projectDir = file("./modules/common")

include(":theme")
project(":theme").projectDir = file("./modules/theme")

include(":api")
project(":api").projectDir = file("./modules/api")

include(":navigation")
project(":navigation").projectDir = file("./modules/navigation")

include(":features")
project(":features").projectDir = file("./modules/features")

include(":features:dashboard")
project(":features:dashboard").projectDir = file("./modules/features/dashboard")
