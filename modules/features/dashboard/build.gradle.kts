plugins {
    id("tech.framti.caml.config.ui")
}

android {
    namespace = "tech.framti.dashboard"
}

dependencies {
    implementation(projects.domain)
    implementation(projects.navigation)
    implementation(projects.theme)
    implementation(projects.common)

    // nav
    implementation(libs.androidx.navigation3.ui)
    implementation(libs.androidx.navigation3.runtime)
    implementation(libs.androidx.lifecycle.viewmodel.navigation3)
    implementation(libs.androidx.hilt.navigation.compose)

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}