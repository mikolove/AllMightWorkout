import com.mikolove.convention.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

class AndroidLibraryFirebaseConventionPlugin : Plugin<Project> {

    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                //apply("com.google.gms.google-services")

            }

            //val extension = extensions.getByType<ApplicationExtension>()
            //configureFirebase(extension)

            dependencies{
                val bom = libs.findLibrary("firebase-bom").get()
                "implementation"(platform(bom))

                "implementation"(libs.findLibrary("firebase-auth-ktx").get())
                "implementation"(libs.findLibrary("firebase-firestore-ktx").get())
            }
        }
    }
}