import org.jetbrains.kotlin.ir.backend.js.compile

@Suppress("DSL_SCOPE_VIOLATION") // TODO: Remove once KTIJ-19369 is fixed
plugins {
    `kotlin-dsl`
}
group = "com.mikolove.allmightworkout.buildlogic"

dependencies{
    compileOnly(libs.android.gradlePlugin)
    compileOnly(libs.android.tools.common)
    compileOnly(libs.kotlin.gradlePlugin)
    compileOnly(libs.ksp.gradlePlugin)
    compileOnly(libs.kapt.gradlePlugin)
    compileOnly(libs.room.gradlePlugin)
    compileOnly(libs.hilt.gradlePlugin)
    compileOnly(libs.firebase.crashlytics.gradlePlugin)
    compileOnly(libs.firebase.performance.gradlePlugin)
}

gradlePlugin{
    plugins {
        register("androidApplication"){
            id ="allmightworkout.android.application"
            implementationClass = "AndroidApplicationConventionPlugin"
        }
        register("androidApplicationCompose"){
            id ="allmightworkout.android.application.compose"
            implementationClass = "AndroidApplicationComposeConventionPlugin"
        }
        register("androidLibrary"){
            id ="allmightworkout.android.library"
            implementationClass = "AndroidLibraryConventionPlugin"
        }
        register("androidLibraryCompose"){
            id ="allmightworkout.android.library.compose"
            implementationClass = "AndroidLibraryComposeConventionPlugin"
        }
        register("androidFeatureUi"){
            id ="allmightworkout.android.feature.ui"
            implementationClass = "AndroidFeatureUiConventionPlugin"
        }
        register("androidRoom"){
            id ="allmightworkout.android.room"
            implementationClass = "AndroidRoomConventionPlugin"
        }
        register("jvmLibrary"){
            id ="allmightworkout.jvm.library"
            implementationClass = "JvmLibraryConventionPlugin"
        }
        register("androidHilt"){
            id ="allmightworkout.android.hilt"
            implementationClass = "AndroidHiltConventionPlugin"
        }
        register("androidFirebase"){
            id ="allmightworkout.android.firebase"
            implementationClass = "AndroidFirebaseConventionPlugin"
        }

    }
}