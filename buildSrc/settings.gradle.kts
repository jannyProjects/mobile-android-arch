// This file is needed because gradle upon every run searches for a settings.gradle or settings.gradle.kts file
// If this file weren't here, gradle would search the entire hierarchy every time which would slow it down
rootProject.buildFileName = "build.gradle.kts"

dependencyResolutionManagement {
    versionCatalogs {
        create("libs") {
            from(files("../gradle/libs.versions.toml"))
        }
    }
}
