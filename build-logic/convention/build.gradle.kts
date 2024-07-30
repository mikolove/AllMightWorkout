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
}

java {
    sourceCompatibility = JavaVersion.VERSION_19
    targetCompatibility = JavaVersion.VERSION_19
}