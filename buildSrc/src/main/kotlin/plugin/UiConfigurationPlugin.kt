package plugin

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.artifacts.VersionCatalogsExtension

class UiConfigurationPlugin: Plugin<Project> {

    override fun apply(project: Project) {
        project.rootProject
            .extensions
            .getByType(VersionCatalogsExtension::class.java)
            .named("libs")


        val libs = project.rootProject.extensions.getByType(VersionCatalogsExtension::class.java).named("libs")

        project.dependencies.apply {
            add("implementation", libs.findLibrary("androidx-compose-bom").get())
            add("implementation", libs.findLibrary("androidx-compose-ui").get())
            add("implementation", libs.findLibrary("androidx-compose-ui-tooling-preview").get())
            add("implementation", libs.findLibrary("androidx-compose-ui-tooling").get())
            add("implementation", libs.findLibrary("androidx-compose-material3").get())
            add("implementation", libs.findLibrary("androidx-lifecycle-viewmodel").get())

            add("implementation", libs.findLibrary("hilt").get())
            add("ksp", libs.findLibrary("hilt-compiler").get())
        }
    }
}