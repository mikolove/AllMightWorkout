package dependencies

object Build {
    val build_tools = "com.android.tools.build:gradle:${Versions.gradle}"
    val kotlin_gradle_plugin = "org.jetbrains.kotlin:kotlin-gradle-plugin:${Versions.kotlin}"
    val navigation_safe_arg = "androidx.navigation:navigation-safe-args-gradle-plugin:${Versions.safe_args}"
    val build_hilt = "com.google.dagger:hilt-android-gradle-plugin:${Versions.hilt}"
    val google_services = "com.google.gms:google-services:${Versions.play_services}"
    val firebase_crashlytics = "com.google.firebase:firebase-crashlytics-gradle:${Versions.build_firebase_crashlytics}"
    val junit5 = "de.mannodermaus.gradle.plugins:android-junit5:1.3.2.0"
}