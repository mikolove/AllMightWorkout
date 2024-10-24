plugins {
    alias(libs.plugins.allmightworkout.android.library)
}

android {
    namespace = "com.mikolove.core.data"
}

dependencies {

    implementation(projects.core.domain)
    implementation(projects.core.database)
    implementation(libs.kotlinx.coroutines.core)

    implementation(libs.androidx.work)
    implementation(libs.bundles.koin)

}