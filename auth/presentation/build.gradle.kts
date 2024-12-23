plugins {
    alias(libs.plugins.allmightworkout.android.feature.ui)
}

android {
    namespace = "com.mikolove.auth.presentation"
}

dependencies {

    implementation(projects.core.domain)
    implementation(projects.auth.domain)

    // Timber
    implementation(libs.timber)
    implementation(libs.androidx.credentials)
    implementation(libs.androidx.credentials.play.services)
}