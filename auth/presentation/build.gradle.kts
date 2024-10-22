plugins {
    alias(libs.plugins.allmightworkout.android.feature.ui)
}

android {
    namespace = "com.mikolove.auth.presentation"
}

dependencies {

    implementation(libs.bundles.koin)

    implementation(projects.core.domain)
    implementation(projects.auth.domain)
}