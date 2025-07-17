plugins {
    alias(libs.plugins.allmightworkout.android.library.compose)

}

android {
    namespace = "com.mikolove.core.presentation.designsystem"
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.graphics)
    implementation(libs.androidx.compose.ui.text.google.fonts)
    debugImplementation(libs.androidx.compose.ui.tooling)
    implementation(libs.androidx.compose.ui.tooling.preview)
    api(libs.androidx.compose.material3)
    api(libs.androidx.compose.material3.navigation.suite)
    api(libs.androidx.compose.material3.adaptative)
    api(libs.androidx.compose.material3.adaptative.layout)
    api(libs.androidx.compose.material3.adaptative.navigation)
}