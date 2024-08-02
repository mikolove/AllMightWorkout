import com.android.build.gradle.LibraryExtension
import com.mikolove.convention.ExtensionType
import com.mikolove.convention.configureBuildTypes
import com.mikolove.convention.configureKotlinAndroid
import com.mikolove.convention.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.kotlin

class AndroidLibraryConventionPlugin : Plugin<Project> {

    override fun apply(target: Project) {
        target.run{
            pluginManager.run {
                apply("com.android.library")
                apply("org.jetbrains.kotlin.android")
                apply("kotlin-parcelize")
                apply("com.google.devtools.ksp")
            }

            extensions.configure<LibraryExtension>{

                configureKotlinAndroid(this)

                configureBuildTypes(
                    commonExtension = this,
                    extensionType = ExtensionType.LIBRARY
                )

                defaultConfig{
                    testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
                    consumerProguardFiles("consumer-rules.pro")
                }

            }

            dependencies{
                "testImplementation"(kotlin("test"))
            }
        }
    }
}