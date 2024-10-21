plugins {
    alias(libs.plugins.allmightworkout.android.library)

}

android {
    namespace = "com.mikolove.analytics.data"

}

dependencies {

    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.bundles.koin)

    implementation(projects.core.domain)
    implementation(projects.core.database)
    implementation(projects.core.network)
    implementation(projects.analytics.domain)
}