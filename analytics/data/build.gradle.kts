plugins {
    alias(libs.plugins.allmightworkout.android.library)
    alias(libs.plugins.allmightworkout.android.hilt)
}

android {
    namespace = "com.mikolove.analytics.data"

}

dependencies {

        implementation(projects.core.domain)
        implementation(projects.core.database)
        implementation(projects.core.network)
        implementation(projects.analytics.domain)

        implementation(libs.kotlinx.coroutines.core)
}