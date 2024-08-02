plugins {
    alias(libs.plugins.allmightworkout.android.library)
    alias(libs.plugins.allmightworkout.android.hilt)
}

android {
    namespace = "com.mikolove.exercise.data"

}

dependencies {

    implementation(projects.core.domain)
    implementation(projects.core.database)
    implementation(projects.core.network)
    implementation(projects.exercise.domain)

    implementation(libs.kotlinx.coroutines.core)
}