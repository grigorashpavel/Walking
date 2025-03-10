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
            versionNameSuffix = ApplicationBuildSettings.versionName
            applicationIdSuffix = ApplicationBuildSettings.BuildType.Debug.getName()

            buildConfigField("String", "ENV", "\"${ApplicationBuildSettings.BuildType.Debug.name}\"")
        }

        create(ApplicationBuildSettings.BuildType.Dev.getName()) {
            initWith(getByName(ApplicationBuildSettings.BuildType.Debug.getName()))
            isDebuggable = false
            versionNameSuffix = ApplicationBuildSettings.versionName
            matchingFallbacks += ApplicationBuildSettings.BuildType.Debug.getName()

            buildConfigField("String", "ENV", "\"${ApplicationBuildSettings.BuildType.Dev.name}\"")
        }

        create(ApplicationBuildSettings.BuildType.Prod.getName()) {
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(
                extension.getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
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

    extension.buildTypes.getByName(ApplicationBuildSettings.BuildType.Prod.getName()).apply {
        signingConfig = extension.signingConfigs.getByName("release")
    }
}