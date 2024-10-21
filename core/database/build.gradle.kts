plugins {
    alias(libs.plugins.allmightworkout.android.library)
    alias(libs.plugins.allmightworkout.android.room)
}

android {
    namespace = "com.mikolove.core.database"
}

dependencies {

    implementation(libs.bundles.koin)

    implementation(projects.core.domain)
    implementation(libs.kotlinx.serialization.json)
}