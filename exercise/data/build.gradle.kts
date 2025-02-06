plugins {
    alias(libs.plugins.allmightworkout.android.library)
}

android {
    namespace = "com.mikolove.exercise.data"

}

dependencies {

    implementation(projects.core.data)
    implementation(projects.core.domain)
    implementation(projects.core.database)
    implementation(projects.core.network)
    implementation(projects.exercise.domain)

    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.bundles.koin)
}