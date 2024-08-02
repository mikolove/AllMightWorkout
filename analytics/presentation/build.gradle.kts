plugins {
    alias(libs.plugins.allmightworkout.android.feature.ui)
}

android {
    namespace = "com.mikolove.analytics.presentation"

}

dependencies {
    implementation(projects.core.domain)
    implementation(projects.analytics.domain)
}