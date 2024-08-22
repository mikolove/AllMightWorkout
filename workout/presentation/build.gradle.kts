plugins {
    alias(libs.plugins.allmightworkout.android.feature.ui)
}

android {
    namespace = "com.mikolove.workout.presentation"

    ksp{
        arg("compose-destinations.moduleName", "workout")
        arg("compose-destinations.mode", "destinations")
    }

}


dependencies {
    implementation(projects.core.domain)
    implementation(projects.workout.domain)

}