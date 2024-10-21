plugins {
    alias(libs.plugins.allmightworkout.android.library)

}

android {
    namespace = "com.mikolove.workout.data"
}

dependencies {

    implementation(projects.core.domain)
    implementation(projects.core.database)
    implementation(projects.core.network)
    implementation(projects.workout.domain)

    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.bundles.koin)
}