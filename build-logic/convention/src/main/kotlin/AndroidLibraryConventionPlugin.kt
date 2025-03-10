import com.android.build.api.dsl.LibraryExtension
import convention.ApplicationBuildSettings
import convention.configureAndroidCommon
import convention.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure

class AndroidLibraryConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply(libs.plugins.android.library.get().pluginId)
            }

            extensions.configure<LibraryExtension> {
                configureAndroidCommon(this)
                configureAndroidLibrary(this)
            }
        }
    }
}

private fun Project.configureAndroidLibrary(
    extension: LibraryExtension,
) {
    extension.apply {
        defaultConfig {
            testOptions.targetSdk = ApplicationBuildSettings.targetSdk
        }
    }
}