plugins {
    alias(libs.plugins.mappie.api.plugin)
    alias(libs.plugins.jetbrains.kotlin.serialization)
}

android {
    namespace = "tech.framti.data"
   }

mappie {
//    useDefaultArguments = false // Disable using default arguments in implicit mappings
//    strictness {
//        visibility = true // Allow calling constructors not visible from the calling scope
//        enums = false // Do not report an error if not all enum sources are mapped
//    }
    reporting {
        enabled = true // Enable report generation
    }
}

dependencies {
    implementation(projects.domain)
    implementation(projects.api)

    implementation(libs.hilt)
    ksp(libs.hilt.compiler)

    implementation(libs.androidx.security.crypto)
    implementation(libs.androidx.datastore.preferences)
    implementation(libs.kotlinx.serialization.core)
    implementation(libs.kotlinx.serialization.json)

    // Network
    implementation(platform(libs.okhttp.bom))
    implementation(libs.okhttp)
    // Retrofit
    implementation(libs.retrofit.runtime)
    implementation(libs.retrofit.moshi)

    implementation(libs.mappie.api)
    implementation(libs.timber)

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}