plugins {
    id("tech.framti.caml.config.ui")
}

android {
    namespace = "tech.framti.common"
}
dependencies {
    implementation(projects.navigation)
    implementation(projects.theme)
    implementation(projects.domain)
    implementation(libs.timber)

    implementation(libs.androidx.compose.material.icons.extended)

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.activity.compose)
    implementation(libs.material)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}