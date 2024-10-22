plugins {
    alias(libs.plugins.allmightworkout.android.library)
    alias(libs.plugins.allmightworkout.android.library.firebase)
}

android {
    namespace = "com.mikolove.auth.data"
}

dependencies {

    implementation(libs.bundles.koin)

    implementation(projects.core.domain)
    implementation(projects.core.data)
    implementation(projects.auth.domain)
}