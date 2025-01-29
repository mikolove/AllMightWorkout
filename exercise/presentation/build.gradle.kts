plugins {
    alias(libs.plugins.allmightworkout.android.feature.ui)
}

android {
    namespace = "com.mikolove.exercise.presentation"

}

dependencies {

    implementation(projects.core.domain)
    implementation(projects.exercise.domain)

    // Timber
    implementation(libs.timber)
}