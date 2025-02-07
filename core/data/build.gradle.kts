plugins {
    alias(libs.plugins.allmightworkout.android.library)
    alias(libs.plugins.allmightworkout.android.library.firebase)
}

android {
    namespace = "com.mikolove.core.data"
}

dependencies {

    implementation(projects.core.domain)
    implementation(projects.core.database)
    implementation(libs.kotlinx.coroutines.core)

    implementation(libs.androidx.work)
    implementation(libs.kotlinx.serialization.json)
    implementation(libs.bundles.koin)
    implementation(libs.timber)

}