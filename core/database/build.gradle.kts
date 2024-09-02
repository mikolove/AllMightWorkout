plugins {
    alias(libs.plugins.allmightworkout.android.library)
    alias(libs.plugins.allmightworkout.android.hilt)
    alias(libs.plugins.allmightworkout.android.room)
}

android {
    namespace = "com.mikolove.core.database"
}

dependencies {

    implementation(projects.core.domain)

}