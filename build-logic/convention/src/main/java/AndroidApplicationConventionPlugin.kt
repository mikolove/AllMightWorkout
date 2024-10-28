import com.android.build.api.dsl.ApplicationExtension
import com.mikolove.convention.ExtensionType
import com.mikolove.convention.configureBuildTypes
import com.mikolove.convention.configureKotlinAndroid
import com.mikolove.convention.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure

class AndroidApplicationConventionPlugin : Plugin<Project> {

    override fun apply(target: Project) {
         target.run{
             pluginManager.run {
                 apply("com.android.application")
                 apply("org.jetbrains.kotlin.android")
             }

             extensions.configure<ApplicationExtension>{

                 defaultConfig {
                    //Required for minSdkVersion to 20 or lower
                     multiDexEnabled = true
                     applicationId = libs.findVersion("projectApplicationId").get().toString()
                     targetSdk = libs.findVersion("projectTargetSdkVersion").get().toString().toInt()

                     versionCode = libs.findVersion("projectVersionCode").get().toString().toInt()
                     versionName = libs.findVersion("projectVersionName").get().toString()
                 }

                 configureKotlinAndroid(this)

                 configureBuildTypes(this, ExtensionType.APPLICATION)
             }
         }
    }
}