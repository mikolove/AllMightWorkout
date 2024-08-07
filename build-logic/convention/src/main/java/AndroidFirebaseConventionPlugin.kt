import androidx.room.gradle.RoomExtension
import com.mikolove.convention.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies

class AndroidFirebaseConventionPlugin : Plugin<Project> {

    override fun apply(target: Project) {
        target.run{
            pluginManager.run {
                apply("com.google.gms.google-services")
                apply("com.google.firebase.crashlytics")
            }

            dependencies{
                val bom = libs.findLibrary("firebase-bom").get()
                "implementation"(platform(bom))

                "implementation"(libs.findLibrary("firebase-analytics").get())
                "implementation"(libs.findLibrary("firebase-crashlytics").get())
            }
        }
    }
}