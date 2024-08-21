plugins {
    alias(libs.plugins.allmightworkout.android.feature.ui)
}

android {
    namespace = "com.mikolove.workout.presentation"
}

ksp{
    arg("compose-destinations.workout", "profile")
}
dependencies {
    implementation(projects.core.domain)
    implementation(projects.workout.domain)

}