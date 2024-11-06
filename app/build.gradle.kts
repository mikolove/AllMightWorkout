plugins {
    alias(libs.plugins.allmightworkout.android.application.compose)
    alias(libs.plugins.allmightworkout.android.application.firebase)
    alias(libs.plugins.allmightworkout.android.library.firebase)
}


android {

    namespace = "com.mikolove.allmightworkout"

    defaultConfig {
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }

}

dependencies {

    // Core
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)

    //Serialization
    implementation(libs.kotlinx.serialization.json)

    //Compose
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.material.icons.extended)
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.graphics)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.compose.material3.navigation.suite)

    implementation(libs.androidx.lifecycle.viewmodel.compose)
    implementation(libs.androidx.lifecycle.runtime.compose)
    implementation(libs.androidx.navigation.compose)

    //Coroutines
    //implementation(libs.kotlinx.coroutines.android)
    //implementation(libs.kotlinx.coroutines.play.services)
    //Support
    //implementation(libs.androidx.appcompat) -> not sure

    //Di
   implementation(libs.bundles.koin)

    //Multidex
    //implementation(libs.androidx.multidex) -> not sure

    // Splash screen
    implementation(libs.androidx.core.splashscreen)

    //WorkManager
    implementation(libs.androidx.work)

    //implementation(libs.annotation)

    // Crypto
    implementation(libs.androidx.security.crypto.ktx)

    //Leakcanary
    debugImplementation(libs.squareup.leakcanary)

    // Timber
    implementation(libs.timber)

    //JUNIT Test
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(libs.androidx.compose.ui.test.junit4)
    debugImplementation(libs.androidx.compose.ui.tooling)
    debugImplementation(libs.androidx.compose.ui.test.manifest)

    //App modules
    implementation(projects.core.presentation.designsystem)
    implementation(projects.core.presentation.ui)
    implementation(projects.core.domain)
    implementation(projects.core.data)
    implementation(projects.core.database)
    implementation(projects.core.network)
    implementation(projects.core.interactors)

    implementation(projects.auth.presentation)
    implementation(projects.auth.data)
    implementation(projects.auth.domain)

    implementation(projects.analytics.presentation)
    implementation(projects.analytics.data)
    implementation(projects.analytics.domain)

    implementation(projects.workout.presentation)
    implementation(projects.workout.data)
    implementation(projects.workout.domain)

    implementation(projects.exercise.presentation)
    implementation(projects.exercise.data)
    implementation(projects.exercise.domain)

}
