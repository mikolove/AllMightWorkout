plugins {
    alias(libs.plugins.allmightworkout.android.library)
}

android {
    namespace = "com.mikolove.exercise.data"

}

dependencies {

    implementation(libs.kotlinx.coroutines.core)
    //implementation(libs.bundles.koin)
    implementation(libs.koin.android.workmanager)

    implementation(libs.kotlinx.coroutines.guava)
    implementation(libs.androidx.work)
    implementation(libs.kotlinx.serialization.json)
    implementation(libs.timber)

    implementation(projects.core.data)
    implementation(projects.core.domain)
    implementation(projects.core.database)
    implementation(projects.core.network)
    implementation(projects.exercise.domain)

}
