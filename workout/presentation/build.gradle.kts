plugins {
    alias(libs.plugins.allmightworkout.android.feature.ui)
}

android {
    namespace = "com.mikolove.workout.presentation"
}


dependencies {
    implementation(libs.timber)
    
    implementation(projects.core.domain)
    implementation(projects.workout.domain)

}