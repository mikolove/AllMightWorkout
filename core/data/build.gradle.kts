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
    implementation(projects.core.interactors)
    implementation(libs.kotlinx.coroutines.core)

    implementation(libs.androidx.work)

    implementation(libs.squareup.retrofit)
}