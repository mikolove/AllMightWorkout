package dependencies


object Dependencies {

    val kotlin_standard_library = "org.jetbrains.kotlin:kotlin-stdlib-jdk7:${Versions.kotlin}"
    val kotlin_reflect = "org.jetbrains.kotlin:kotlin-reflect:${Versions.kotlin}"
    val ktx = "androidx.core:core-ktx:${Versions.ktx}"

    val kotlin_coroutines = "org.jetbrains.kotlinx:kotlinx-coroutines-core:${Versions.coroutines_version}"
    val kotlin_coroutines_android = "org.jetbrains.kotlinx:kotlinx-coroutines-android:${Versions.coroutines_version}"
    val kotlin_coroutines_play_services = "org.jetbrains.kotlinx:kotlinx-coroutines-play-services:${Versions.coroutines_play_services}"

    val recycler_view = "androidx.recyclerview:recyclerview:${Versions.recycler_view}"


    val lifecycle_runtime = "androidx.lifecycle:lifecycle-runtime:${Versions.lifecycle_version}"
    val lifecycle_livedata = "androidx.lifecycle:lifecycle-livedata-ktx:${Versions.lifecycle_version}"
    val lifecycle_viewmodel = "androidx.lifecycle:lifecycle-viewmodel-ktx:${Versions.lifecycle_version}"

    val hilt = "com.google.dagger:hilt-android:${Versions.hilt}"

    val hilt_jetpack_navigation = "androidx.hilt:hilt-navigation-fragment:${Versions.hilt_jetpack}"
    val hilt_jetpack_compose = "androidx.hilt:hilt-navigation-compose:${Versions.hilt_jetpack}"
    val hilt_workmanager = "androidx.hilt:hilt-work:${Versions.hilt_jetpack}"

    val fragment_ktx = "androidx.fragment:fragment-ktx:${Versions.fragment_version}"

    val navigation_fragment = "androidx.navigation:navigation-fragment-ktx:${Versions.nav_components}"
    val navigation_runtime = "androidx.navigation:navigation-runtime:${Versions.nav_components}"
    val navigation_ui = "androidx.navigation:navigation-ui-ktx:${Versions.nav_components}"
    val navigation_dynamic = "androidx.navigation:navigation-dynamic-features-fragment:${Versions.nav_components}"

    val material_dialogs = "com.afollestad.material-dialogs:core:${Versions.material_dialogs}"
    val material_dialogs_input = "com.afollestad.material-dialogs:input:${Versions.material_dialogs}"

    val room_runtime = "androidx.room:room-runtime:${Versions.room}"
    val room_ktx = "androidx.room:room-ktx:${Versions.room}"


    val play_core = "com.google.android.play:core:${Versions.play_core}"
    val leak_canary = "com.squareup.leakcanary:leakcanary-android:${Versions.leak_canary}"

    val firebase_auth_ui = "com.firebaseui:firebase-ui-auth:${Versions.firebase_ui_auth}"
    val firebase_firestore = "com.google.firebase:firebase-firestore-ktx:${Versions.firebase_firestore}"
    val firebase_auth = "com.google.firebase:firebase-auth:${Versions.firebase_auth}"
    val firebase_analytics = "com.google.firebase:firebase-analytics:${Versions.firebase_analytics}"
    val firebase_crashlytics = "com.crashlytics.sdk.android:crashlytics:${Versions.firebase_crashlytics}"
    val firebase_bom = "com.google.firebase:firebase-bom:${Versions.firebase_bom}"
    val firebase_analytics_bom = "com.google.firebase:firebase-analytics-ktx"
    val firebase_crashlytics_bom = "com.google.firebase:firebase-crashlytics-ktx"
    val firebase_auth_bom = "com.google.firebase:firebase-auth-ktx"
    val firebase_firestore_bom = "com.google.firebase:firebase-firestore-ktx"


    val retrofit = "com.squareup.retrofit2:retrofit:${Versions.retrofit2_version}"
    val retrofit_gson = "com.squareup.retrofit2:converter-gson:${Versions.retrofit2_version}"

    val multidex = "androidx.multidex:multidex:${Versions.multidex_version}"

    val datastore = "androidx.datastore:datastore-preferences:${Versions.datastore}"


    val compose_bom = "androidx.compose:compose-bom:${Versions.compose_bom}"
    val compose_material3 = "androidx.compose.material3:${Versions.material3}"
    val compose_uitooling_preview = "androidx.compose.ui:ui-tooling-preview"
    val compose_uitooling = "androidx.compose.ui:ui-tooling"
}