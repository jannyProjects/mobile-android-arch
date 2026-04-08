package plugin

import Versions
import android
import main
import org.gradle.api.JavaVersion
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.tasks.compile.JavaCompile
import org.gradle.kotlin.dsl.withType
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import variants
import versionName

class ProjectConfigurationPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        target.subprojects {
            tasks.withType<JavaCompile> {
                options.compilerArgs.addAll(listOf("-Xmaxerrs", Int.MAX_VALUE.toString()))
            }

            tasks.configureEach {
                if (name.contains("AndroidTest")) {
                    enabled = false
                }
            }

            tasks.withType<KotlinCompile> {
                compilerOptions {
                    freeCompilerArgs.addAll(
                        "-opt-in=kotlinx.coroutines.ExperimentalCoroutinesApi",
                        "-opt-in=kotlinx.coroutines.FlowPreview",
                        "-Xjvm-default=all"
                    )
                }
            }
        }

        target.configure(target.subprojects.filter { it.isAndroidModule() }) {
            configureAndroidModule()
        }
        target.configure(target.subprojects.filter { it.hasUi() }) {
            configureUiModule()
        }
    }

    private fun Project.configureAndroidModule() {
        if (name == "app") {
            plugins.apply("com.android.application")
        } else {
            plugins.apply("com.android.library")
        }

        plugins.apply("org.jetbrains.kotlin.android")
        plugins.apply("com.google.devtools.ksp")

        android {
            compileSdkVersion(Versions.targetsdk)

            compileOptions {
                sourceCompatibility = JavaVersion.VERSION_17
                targetCompatibility = JavaVersion.VERSION_17
            }

            defaultConfig {
                minSdk = Versions.minsdk
                targetSdk = Versions.targetsdk

                versionCode = 1
                versionName = project.versionName

                resourceConfigurations.addAll(
                    listOf("en")
                )

                vectorDrawables.setGeneratedDensities(emptyList())

                if (this@configureAndroidModule.name != "app" && file("proguard-rules.pro").exists()) {
                    consumerProguardFiles("proguard-rules.pro")
                }
            }

            flavorDimensions("default")

            productFlavors.configureEach {
                dimension = "default"

                sourceSets.getByName(name) {
                    java.srcDir("src/$name/kotlin")
                }
            }

            buildTypes.configureEach {
                sourceSets.getByName(name) {
                    java.srcDir("src/$name/kotlin")
                }
            }

            variants { variant ->
                android.sourceSets.getByName(variant.name) {
                    java.srcDir("src/${variant.name}/kotlin")
                }
            }

            sourceSets.main.java.srcDir("src/main/kotlin")
        }
    }

    private fun Project.configureUiModule() {
        android {
            compileOptions {
                sourceCompatibility = JavaVersion.VERSION_17
                targetCompatibility = JavaVersion.VERSION_17
            }

            defaultConfig {
                vectorDrawables.setGeneratedDensities(emptyList())
            }
        }

        plugins.apply("org.jetbrains.kotlin.plugin.compose")
        plugins.apply("com.google.dagger.hilt.android")
    }

    private fun Project.isAndroidModule() = file("src/main/AndroidManifest.xml").exists()

    private fun Project.hasUi() =
        parent?.name == "features" || name == "app" || name == "theme" || name == "navigation" || name == "common"
}