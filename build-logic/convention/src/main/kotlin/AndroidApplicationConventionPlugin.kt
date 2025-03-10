import com.android.build.api.dsl.ApplicationExtension
import convention.ApplicationBuildSettings
import convention.configureAndroidCommon
import convention.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure

class AndroidApplicationConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply(libs.plugins.android.application.get().pluginId)
            }

            extensions.configure<ApplicationExtension> {
                configureAndroidCommon(this)
                configureAndroidApplication(this)
            }
        }
    }
}

private fun Project.configureAndroidApplication(
    extension: ApplicationExtension,
) {
    extension.apply {
        defaultConfig {
            applicationId = ApplicationBuildSettings.aaplicationId
            versionCode = ApplicationBuildSettings.versionCode
            versionName = ApplicationBuildSettings.versionName
            targetSdk = ApplicationBuildSettings.targetSdk
        }
    }
}