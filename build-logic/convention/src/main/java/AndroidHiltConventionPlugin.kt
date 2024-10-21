import com.mikolove.convention.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

/*class AndroidHiltConventionPlugin : Plugin<Project> {

    override fun apply(target: Project) {
        target.run{
            pluginManager.run {
                apply("kotlin-kapt")
                apply("com.google.dagger.hilt.android")
            }

            dependencies{
                "implementation"(libs.findLibrary("dagger.hilt").get())
                "kapt"(libs.findLibrary("dagger.hilt.compiler").get())
                "kapt"(libs.findLibrary("dagger.hilt.jetpack.compiler").get())
                "implementation"(libs.findLibrary("dagger.hilt.navigation.compose").get())
                "implementation"(libs.findLibrary("dagger.hilt.workmanager").get())

                "androidTestImplementation"(libs.findLibrary("dagger.hilt.testing").get())
                "kaptAndroidTest"(libs.findLibrary("dagger.hilt.compiler").get())
                "testImplementation"(libs.findLibrary("dagger.hilt.testing").get())
                "kaptTest"(libs.findLibrary("dagger.hilt.compiler").get())
            }
        }
    }
}*/