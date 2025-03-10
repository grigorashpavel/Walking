import com.android.build.api.dsl.ApplicationExtension
import com.android.build.api.dsl.CommonExtension
import com.android.build.api.dsl.LibraryExtension
import convention.ApplicationBuildSettings
import convention.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.plugins.ExtensionAware
import org.jetbrains.kotlin.gradle.dsl.KotlinJvmOptions

class AndroidKotlinConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply(libs.plugins.kotlin.android.get().pluginId)
            }

            extensions.findByType(ApplicationExtension::class.java)?.apply {
                configureKotlin(this)
            }
            extensions.findByType(LibraryExtension::class.java)?.apply {
                configureKotlin(this)
            }
        }
    }
}

private fun Project.configureKotlin(
    commonExtension: CommonExtension<*, *, *, *, *, *>,
) {
    commonExtension.apply {
        compileOptions {
            sourceCompatibility = ApplicationBuildSettings.javaVersion
            targetCompatibility = ApplicationBuildSettings.javaVersion
        }

        kotlinOptions {
            jvmTarget = ApplicationBuildSettings.jvmTarget
        }
    }
}

internal fun CommonExtension<*, *, *, *, *, *>.kotlinOptions(block: KotlinJvmOptions.() -> Unit) {
    (this as ExtensionAware).extensions.configure("kotlinOptions", block)
}