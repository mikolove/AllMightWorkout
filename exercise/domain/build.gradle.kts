plugins {
    alias(libs.plugins.allmightworkout.jvm.library)
}

dependencies{
    implementation(libs.kotlinx.coroutines.core)
    implementation(project(":core:domain"))
}