plugins {
    alias(libs.plugins.allmightworkout.android.library)
    alias(libs.plugins.allmightworkout.android.library.firebase)
}

android {
    namespace = "com.mikolove.core.network"
}

dependencies {

    implementation(libs.bundles.koin)
    implementation(libs.timber)
    implementation(projects.core.domain)
    implementation(libs.kotlinx.coroutines.core)
}