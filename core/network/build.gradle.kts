plugins {
    alias(libs.plugins.allmightworkout.android.application)
    alias(libs.plugins.allmightworkout.android.application.firebase)
}

android {
    namespace = "com.mikolove.core.network"
}

dependencies {

    implementation(projects.core.domain)
    implementation(projects.core.data)
    implementation(libs.kotlinx.coroutines.core)

}