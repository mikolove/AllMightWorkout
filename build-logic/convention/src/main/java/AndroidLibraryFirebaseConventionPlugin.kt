import com.mikolove.convention.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

class AndroidLibraryFirebaseConventionPlugin : Plugin<Project> {

    override fun apply(target: Project) {
        with(target) {


            dependencies{
                val bom = libs.findLibrary("firebase-bom").get()
                "api"(platform(bom))

                "api"(libs.findLibrary("firebase-auth-ktx").get())
                "api"(libs.findLibrary("firebase-firestore-ktx").get())
            }
        }
    }
}