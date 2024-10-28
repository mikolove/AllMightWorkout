plugins {
    alias(libs.plugins.allmightworkout.android.library.compose)
}

android {
    namespace = "com.mikolove.core.presentation.ui"
}

    dependencies {

        val composeBom = platform(libs.androidx.compose.bom)
        implementation(composeBom)
        androidTestImplementation(composeBom)

        implementation(libs.androidx.compose.ui)
        implementation(libs.androidx.compose.ui.graphics)
        implementation(libs.androidx.lifecycle.runtime.compose)
        implementation(libs.androidx.lifecycle.viewmodel.compose)
        implementation(libs.androidx.compose.ui.tooling.preview)
        implementation(libs.androidx.compose.material3)

        implementation(projects.core.domain)
        implementation(projects.core.presentation.designsystem)
    }