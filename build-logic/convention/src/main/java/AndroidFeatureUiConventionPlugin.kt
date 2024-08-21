import com.mikolove.convention.addUiLayerDependencies
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

class AndroidFeatureUiConventionPlugin : Plugin<Project> {

    override fun apply(target: Project) {
        target.run{
            pluginManager.run {
                apply("allmightworkout.android.library.compose")
                apply("allmightworkout.android.hilt")
            }

            dependencies{
                addUiLayerDependencies(target)
            }
        }
    }
}