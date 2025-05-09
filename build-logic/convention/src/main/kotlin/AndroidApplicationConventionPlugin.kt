import com.android.build.api.dsl.ApplicationExtension
import convention.ApplicationBuildSettings
import convention.configureAndroidCommon
import convention.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import java.util.Properties

class AndroidApplicationConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply(libs.plugins.android.application.get().pluginId)
            }

            extensions.configure<ApplicationExtension> {
                configureAndroidCommon(this)
                configureAndroidApplication(this)
                configureBuildTypes(this)
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

private fun Project.configureBuildTypes(extension: ApplicationExtension) {
    extension.buildTypes {
        getByName(ApplicationBuildSettings.BuildType.Debug.getName()) {
            isDebuggable = true

            loadSecrets(ApplicationBuildSettings.BuildType.Debug).forEach { (key, value) ->
                if (key == "YANDEX_CLIENT_ID") {
                    manifestPlaceholders["YANDEX_CLIENT_ID"] = value
                }
                buildConfigField(type = "String", name = key, value = value)
            }

            buildConfigField("String", "ENV", "\"${ApplicationBuildSettings.BuildType.Debug.name}\"")
        }

        create(ApplicationBuildSettings.BuildType.Dev.getName()) {
            initWith(getByName(ApplicationBuildSettings.BuildType.Debug.getName()))
            isDebuggable = false

            loadSecrets(ApplicationBuildSettings.BuildType.Dev).forEach { (key, value) ->
                if (key == "YANDEX_CLIENT_ID") {
                    manifestPlaceholders["YANDEX_CLIENT_ID"] = value
                }
                buildConfigField(type = "String", name = key, value = "\"$value\"")
            }

            buildConfigField("String", "ENV", "\"${ApplicationBuildSettings.BuildType.Dev.name}\"")
        }

        getByName(ApplicationBuildSettings.BuildType.Release.getName()) {

            loadSecrets(ApplicationBuildSettings.BuildType.Release).forEach { (key, value) ->
                if (key == "YANDEX_CLIENT_ID") {
                    manifestPlaceholders["YANDEX_CLIENT_ID"] = value
                }
                buildConfigField(type = "String", name = key, value = "\"$value\"")
            }

            proguardFiles(
                extension.getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
}

private fun Project.loadSecrets(buildType: ApplicationBuildSettings.BuildType): Map<String, String> {
    val secrets = mutableMapOf<String, String>()

    if (buildType != ApplicationBuildSettings.BuildType.Debug) {
        System.getenv().forEach { (key, value) ->
            secrets[key] = value
        }
        return secrets
    }

    val secretsFile = rootProject.file("secrets.properties")
    if (secretsFile.exists()) {
        val props = Properties().apply {
            load(secretsFile.inputStream())
        }
        props.forEach { key, value ->
            secrets[key.toString()] = value.toString()
        }
    }

    return secrets
}

private fun Project.configureSigning(extension: ApplicationExtension) {
    extension.signingConfigs {
        create("release") {
            storeFile = rootProject.file("keystore/release.jks")
            storePassword = System.getenv("STORE_PASSWORD") ?: ""
            keyAlias = System.getenv("KEY_ALIAS") ?: ""
            keyPassword = System.getenv("KEY_PASSWORD") ?: ""
        }
    }

    extension.buildTypes.getByName(ApplicationBuildSettings.BuildType.Release.getName()).apply {
        signingConfig = extension.signingConfigs.getByName(
            ApplicationBuildSettings.BuildType.Release.getName()
        )
    }
}