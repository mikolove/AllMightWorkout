plugins {
    alias(libs.plugins.allmightworkout.android.library)
}

android {
    namespace = "com.mikolove.core.interactors"
}

dependencies {

    implementation(projects.core.domain)
    implementation(projects.core.database)
    implementation(libs.kotlinx.coroutines.core)

}