plugins {
    alias(libs.plugins.allmightworkout.android.library)
    //alias(libs.plugins.allmightworkout.android.library.firebase)
}

android {
    namespace = "com.mikolove.core.data"
}

dependencies {

    implementation(projects.core.domain)
    implementation(projects.core.database)
    api(projects.core.network)

    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.kotlinx.serialization.json)

    implementation(libs.bundles.koin)
    implementation(libs.timber)
}