import convention.libs
import io.gitlab.arturbosch.detekt.Detekt
import io.gitlab.arturbosch.detekt.DetektCreateBaselineTask
import io.gitlab.arturbosch.detekt.extensions.DetektExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.provider.Provider
import org.gradle.kotlin.dsl.DependencyHandlerScope
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.withType


class DetektConventionPlugin : Plugin<Project> {

    override fun apply(project: Project) = with(project) {
        plugins.apply(libs.plugins.detekt.get().pluginId)
        dependencies {
            detekt(libs.detekt.cli)
            detekt(libs.detekt.formatting)
        }
        extensions.configure<DetektExtension> {
            toolVersion = project.libs.versions.detekt.get()
            autoCorrect = true
            parallel = true
            config.setFrom("${project.rootDir}/data/detekt/detekt.yml")

            source.setFrom(
                project.files(
                    "src/main/java",
                    "src/main/kotlin",

                    "src/debug/java",
                    "src/debug/kotlin",

                    "src/release/java",
                    "src/release/kotlin",

                    "src/test/java",
                    "src/test/kotlin",

                    "src/androidTest/java",
                    "src/androidTest/kotlin"
                )
            )
        }

        tasks.withType<Detekt>().configureEach {
            jvmTarget = project.libs.versions.jvmTarget.get()
            reports {
                html.required.set(false)
                xml.required.set(false)
                sarif.required.set(false)
                md.required.set(false)
            }
        }

        tasks.withType<DetektCreateBaselineTask>().configureEach {
            jvmTarget = project.libs.versions.jvmTarget.get()
        }

        tasks.withType<Detekt>().configureEach {
            reports {
                html.required.set(false)
                xml.required.set(false)
                sarif.required.set(false)
                md.required.set(false)
            }
        }
    }
}

private fun DependencyHandlerScope.detekt(provider: Provider<*>) {
    add("detekt", provider)
}
   