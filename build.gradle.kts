plugins {

    /**
     * Use `apply false` in the top-level build.gradle file to add a Gradle
     * plugin as a build dependency but not apply it to the current (root)
     * project. Don't use `apply false` in sub-projects. For more information,
     * see Applying external plugins with same version to subprojects.
     */

    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.jetbrainsKotlinAndroid) apply false
    alias(libs.plugins.android.library) apply false
    alias(libs.plugins.ksp) apply false
    alias(libs.plugins.room) apply false
    alias(libs.plugins.dagger.hilt) apply false
    alias(libs.plugins.android.gms.google.services) apply false
    alias(libs.plugins.kapt) apply false
    alias(libs.plugins.parcelize) apply false
    alias(libs.plugins.org.jetbrains.kotlin.jvm) apply false
    alias(libs.plugins.firebase.crashlytics) apply false

}