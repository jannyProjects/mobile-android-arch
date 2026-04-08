import com.android.build.api.variant.ApplicationAndroidComponentsExtension
import com.android.build.api.variant.LibraryAndroidComponentsExtension
import com.android.build.api.variant.Variant
import com.android.build.gradle.BaseExtension
import org.gradle.api.NamedDomainObjectContainer
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.getByType
import org.json.JSONObject

internal fun Project.android(action: BaseExtension.() -> Unit) = extensions.configure(action)

internal val Project.android
    get() = extensions.getByType<BaseExtension>()

fun Project.getProperty(propertyName: String): String =
    findProperty(propertyName) as? String ?: System.getenv(propertyName)

fun Project.variants(action: (Variant) -> Unit) {
    if (plugins.hasPlugin("android-library")) {
        extensions.getByType(LibraryAndroidComponentsExtension::class.java)
            .onVariants { action(it) }
    } else {
        extensions.getByType(ApplicationAndroidComponentsExtension::class.java)
            .onVariants { action(it) }
    }
}

val <T> NamedDomainObjectContainer<T>.main: T
    get() = getByName("main")

val Project.versionName: String
    get() = JSONObject(rootProject.file("version.json").readText()).getString("version")