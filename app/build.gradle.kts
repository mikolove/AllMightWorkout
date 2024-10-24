plugins {
    //    id ("com.android.application")
   //     id ("org.jetbrains.kotlin.android")
    //alias(libs.plugins.allmightworkout.android.application)
    alias(libs.plugins.allmightworkout.android.application.compose)
    //alias(libs.plugins.allmightworkout.android.hilt)
    alias(libs.plugins.allmightworkout.android.room)
    alias(libs.plugins.allmightworkout.android.application.firebase)
    alias(libs.plugins.kotlin.serialization)
    //id ("kotlin-parcelize")
    //id ("androidx.navigation.safeargs.kotlin") // View Fragment only
    /*used for Lib destination - have to match kotlin version*/
    //id ("com.google.devtools.ksp")
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
    /*tasks.test {
        useJUnitPlatform()
    }*/

    /*    sourceSets {
            test.resources.srcDirs += 'src/test/res'
        }
        testOptions{
            unitTests{
                .all {
                    useJUnitPlatform()
                }
            }
        }*/

}

dependencies {
    //implementation(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar"))))
    //implementation(fileTree(dir: "libs", include: ["*.jar"]))

    //Kotlin
    //implementation Dependencies.kotlin_standard_library
    //implementation Dependencies.kotlin_reflect
    //implementation(Dependencies.ktx)
    //implementation(libs.desugar.jdk.libs)
    implementation(libs.androidx.core.ktx)

    //Firebase
    implementation(libs.firebase.firestore.ktx)
    implementation(libs.firebase.auth.ktx)
    implementation(libs.firebase.auth.ui)

    //implementation(platform(Dependencies.firebase_bom))
    //implementation(Dependencies.firebase_analytics_bom)
    //implementation(Dependencies.firebase_crashlytics_bom)
    //implementation(Dependencies.firebase_firestore_bom)
    //implementation(Dependencies.firebase_auth_bom)
    //implementation "com.google.firebase:firebase-auth-ktx:21.1.0"
    //implementation(Dependencies.firebase_auth_ui)

    //Android S fix auth immutable / mutable flag
    //implementation("com.google.android.gms:play-services-auth:20.4.0")
    //implementation("androidx.work:work-runtime-ktx:2.7.1")

    //Retrofit
    implementation(libs.squareup.retrofit)
    implementation(libs.squareup.retrofit.gson)
    //implementation(Dependencies.retrofit)
    //implementation(Dependencies.retrofit_gson)

    //Coroutines
    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.kotlinx.coroutines.android)
    implementation(libs.kotlinx.coroutines.play.services)
    //implementation(Dependencies.kotlin_coroutines)
    //implementation(Dependencies.kotlin_coroutines_android)
    //implementation(Dependencies.kotlin_coroutines_play_services)

    //Support
    implementation(libs.androidx.appcompat)
    //implementation(SupportDependencies.appcompat)
    //implementation(SupportDependencies.constraintlayout)
    //implementation(SupportDependencies.material_design)
    //implementation(SupportDependencies.swipe_refresh_layout)

    //Lifecycle -- seem outdated now
    implementation(libs.androidx.lifecycle.runtime.compose)
    implementation(libs.androidx.lifecycle.viewmodel.compose)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    //implementation(Dependencies.lifecycle_runtime)
    //implementation(Dependencies.lifecycle_viewmodel)
    //implementation(Dependencies.lifecycle_livedata)

    //Navigation component

    implementation(libs.androidx.navigation.compose)
    implementation(libs.kotlinx.serialization.json)

    //temporary
    //implementation("androidx.navigation:navigation-fragment-ktx:2.7.7")
    //implementation("androidx.navigation:navigation-ui-ktx:2.7.7")

    //In convention plugin
    // implementation(libs.compose.destination.core)
    //ksp(libs.compose.destination.ksp)

    //implementation(Dependencies.navigation_fragment)
    //implementation(Dependencies.navigation_ui)
    //implementation(Dependencies.navigation_dynamic)

    //Recycler View
    //implementation(Dependencies.recycler_view)

    //Fragment
    //implementation(Dependencies.fragment_ktx)

    //Room
    //implementation(libs.room.runtime)
    //implementation(libs.room.ktx)
    //ksp(libs.room.compiler)
    //implementation("androidx.compose.foundation:foundation-android:1.5.4")

    //Material dialog
    //implementation(Dependencies.material_dialogs)
    //implementation(Dependencies.material_dialogs_input)

    //Di
    //Versions are different for hilt and jetpack tools
    //In convention plugin
//    implementation(libs.dagger.hilt)
//    implementation(libs.dagger.hilt.navigation.compose)
//    implementation(libs.dagger.hilt.workmanager)
//    kapt(libs.dagger.hilt.compiler)
//    kapt(libs.dagger.hilt.jetpack.compiler)
    implementation(libs.bundles.koin)

    //Jetpack tools
    //implementation(Dependencies.hilt_jetpack_navigation)
    //implementation(Dependencies.hilt_jetpack_compose)
    //implementation(Dependencies.hilt_workmanager)
    //kapt(AnnotationProcessing.hilt_compiler_androidx)

    //Multidex
    implementation(libs.androidx.multidex)

    //Data Store
    implementation(libs.androidx.datastore)

    // JUNIT Test
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(libs.androidx.compose.ui.test.junit4)
    debugImplementation(libs.androidx.compose.ui.tooling)
    debugImplementation(libs.androidx.compose.ui.test.manifest)

    //testImplementation TestDependencies.mockk
    //testImplementation(TestDependencies.jupiter_api)
    //testRuntimeOnly(TestDependencies.jupiter_engine)
    //testImplementation(TestDependencies.jupiter_params)
    //testImplementation(TestDependencies.junit4)


    //ANDROID TEST
    //androidTestImplementation(AndroidTestDependencies.test_core_ktx)
    //androidTestImplementation(AndroidTestDependencies.test_ext)
    //androidTestImplementation(AndroidTestDependencies.test_ext_truth)
    //androidTestImplementation(AndroidTestDependencies.junit_ktx)
    //androidTestImplementation(AndroidTestDependencies.test_runner)
    //androidTestImplementation(AndroidTestDependencies.test_orchestrator)
    //androidTestImplementation(AndroidTestDependencies.test_rules)
    //debugImplementation(AndroidTestDependencies.monitor)


    /*
    Fix error
    More than one file was found with OS independent path 'META-INF/AL2.0'.
    https://github.com/Kotlin/kotlinx.coroutines/issues/2023

    androidTestImplementation(AndroidTestDependencies.coroutines_test){
        // conflicts with mockito due to direct inclusion of byte buddy
        exclude group: "org.jetbrains.kotlinx", module: "kotlinx-coroutines-debug"
    } */

    //androidTestImplementation(AndroidTestDependencies.kotlin_test)
    //androidTestImplementation(AndroidTestDependencies.espresso_core)
    //androidTestImplementation(AndroidTestDependencies.mockk_android)
    //androidTestImplementation(AndroidTestDependencies.navigation_testing)

    //debugImplementation AndroidTestDependencies.fragment_testing
    //androidTestImplementation AndroidTestDependencies.espresso_contrib
    //androidTestImplementation AndroidTestDependencies.idling_resource


    // Hilt for instrumentation tests
//    androidTestImplementation(libs.dagger.hilt.testing)
//    kaptAndroidTest(libs.dagger.hilt.compiler)

    // Hilt for local unit test
//    testImplementation(libs.dagger.hilt.testing)
//    kaptTest(libs.dagger.hilt.compiler)

    //Test core ktx and fragment scenario
    //debugImplementation AndroidTestDependencies.fragment_testing
    //androidTestImplementation AndroidTestDependencies.text_core_ktx


    //Jetpack compose
    //Injected from convention Plugin
    // val composeBom = platform(libs.androidx.compose.bom)
    //implementation(composeBom)
    //androidTestImplementation(composeBom)
    //debugImplementation(libs.androidx.compose.ui.tooling)
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.graphics)

    implementation(libs.material)
    api(libs.androidx.compose.material3)
    implementation(libs.androidx.material.icons.extended)
    implementation(libs.androidx.compose.ui.tooling.preview)

    //implementation(platform("androidx.compose:compose-bom:2024.01.00"))
    //androidTestImplementation(platform("androidx.compose:compose-bom:2024.01.00"))

    //implementation(Dependencies.compose_material3)
    //implementation(Dependencies.compose_uitooling_preview)
    //debugImplementation(Dependencies.compose_uitooling)

   //implementation(Dependencies.compose_lifecycle_runtime)
    //implementation(Dependencies.compose_viewmodel)
    //implementation(Dependencies.compose_lvm_ss)

    //implementation(Dependencies.paging)
    //implementation(Dependencies.paging_compose)

    //Lib destination nav
    //implementation Dependencies.compose_navigation
    //implementation(Dependencies.compose_destination_core)
    //ksp(Dependencies.compose_destination_ksp)

    implementation(libs.androidx.work)

    implementation(libs.annotation)

    debugImplementation(libs.squareup.leakcanary)


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

/*kapt {
    correctErrorTypes = true
}*/

/*
repositories {
    mavenCentral()
}*/
