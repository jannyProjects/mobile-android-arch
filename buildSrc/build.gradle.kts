plugins {
    `kotlin-dsl`
}

gradlePlugin {
    plugins {
        create("config") {
            id = "tech.framti.caml.config"
            implementationClass = "plugin.ProjectConfigurationPlugin"
        }
        create("config.ui") {
            id = "tech.framti.caml.config.ui"
            implementationClass = "plugin.UiConfigurationPlugin"
        }
    }
}

repositories {
    google()
    mavenCentral()
}

dependencies {
    implementation(libs.gradle.plugin)
    implementation(libs.gradle.api)
    implementation(libs.kotlin.gradle.plugin)

    implementation(libs.androidx.compose.gradle.plugin)
    implementation(libs.hilt.gradle.plugin)
    implementation(libs.kotlin.ksp)

    implementation(libs.json)
}
