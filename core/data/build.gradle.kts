plugins {
    alias(libs.plugins.allmightworkout.android.library)
    alias(libs.plugins.allmightworkout.android.hilt)
}

android {
    namespace = "com.mikolove.core.data"
}

dependencies {

    implementation(projects.core.domain)
    implementation(projects.core.database)
    implementation(libs.kotlinx.coroutines.core)

    implementation(libs.squareup.retrofit)
}