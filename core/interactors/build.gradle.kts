plugins {
    alias(libs.plugins.allmightworkout.android.library)
}

android {
    namespace = "com.mikolove.core.interactors"
}

dependencies {

    implementation(projects.core.domain)
    implementation(projects.core.data)
    implementation(libs.kotlinx.coroutines.core)

}