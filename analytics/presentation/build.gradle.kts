plugins {
    alias(libs.plugins.allmightworkout.android.feature.ui)
}

android {
    namespace = "com.mikolove.analytics.presentation"

    ksp{
        arg("compose-destinations.moduleName", "analytics")
        arg("compose-destinations.mode", "destinations")
    }
}

dependencies {
    implementation(projects.core.domain)
    implementation(projects.analytics.domain)
}