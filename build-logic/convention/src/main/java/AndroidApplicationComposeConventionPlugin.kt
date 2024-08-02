import com.android.build.api.dsl.ApplicationExtension
import com.mikolove.convention.configureAndroidCompose
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.getByType

class AndroidApplicationComposeConventionPlugin : Plugin<Project> {

    override fun apply(target: Project) {
        target.run{

            pluginManager.run{
                apply("allmightworkout.android.application")
                apply("com.google.devtools.ksp")
            }

            val extension = extensions.getByType<ApplicationExtension>()

            configureAndroidCompose(extension)
        }
    }
}