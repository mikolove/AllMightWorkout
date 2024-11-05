import com.android.build.api.dsl.ApplicationExtension
import com.mikolove.convention.configureFirebase
import com.mikolove.convention.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.getByType

class AndroidApplicationFirebaseConventionPlugin : Plugin<Project> {

    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply("com.google.gms.google-services")
                apply("com.google.firebase.crashlytics")
                apply("com.google.firebase.firebase-perf")
            }

            val extension = extensions.getByType<ApplicationExtension>()
            configureFirebase(extension)

            dependencies{
                val bom = libs.findLibrary("firebase-bom").get()
                "implementation"(platform(bom))

                "implementation"(libs.findLibrary("firebase-analytics").get())
                "implementation"(libs.findLibrary("firebase-performance").get())
                "implementation"(libs.findLibrary("firebase-crashlytics").get())
            }
        }
    }
}