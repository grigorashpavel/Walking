import com.android.build.api.dsl.ApplicationExtension
import convention.ApplicationBuildSettings
import convention.configureAndroidCommon
import convention.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import java.io.FileNotFoundException
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
                    manifestPlaceholders["YANDEX_CLIENT_ID"] =
                        value.removePrefix("\"").removeSuffix("\"")
                }
                buildConfigField(type = "String", name = key, value = value)
            }

            buildConfigField(
                "String",
                "ENV",
                "\"${ApplicationBuildSettings.BuildType.Debug.name}\""
            )
            configureSigning(extension, loadSignSecrets(ApplicationBuildSettings.BuildType.Debug))
        }

        create(ApplicationBuildSettings.BuildType.Dev.getName()) {
            initWith(getByName(ApplicationBuildSettings.BuildType.Debug.getName()))
            isDebuggable = false

            loadSecrets(ApplicationBuildSettings.BuildType.Dev).forEach { (key, value) ->
                if (key == "YANDEX_CLIENT_ID") {
                    manifestPlaceholders["YANDEX_CLIENT_ID"] =
                        value.removePrefix("\"").removeSuffix("\"")
                }
                buildConfigField(type = "String", name = key, value = "\"$value\"")
            }

            buildConfigField("String", "ENV", "\"${ApplicationBuildSettings.BuildType.Dev.name}\"")
        }

        getByName(ApplicationBuildSettings.BuildType.Release.getName()) {

            loadSecrets(ApplicationBuildSettings.BuildType.Release).forEach { (key, value) ->
                if (key == "YANDEX_CLIENT_ID") {
                    manifestPlaceholders["YANDEX_CLIENT_ID"] =
                        value.removePrefix("\"").removeSuffix("\"")
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

private data class SignSecrets(
    val key: String,
    val storePassword: String,
    val keyPassword: String,
)

private fun Project.loadSignSecrets(buildType: ApplicationBuildSettings.BuildType): SignSecrets {
    val secretsFile = rootProject.file("secrets.properties")
    if (secretsFile.exists()) {
        val props = Properties().apply {
            load(secretsFile.inputStream())
        }

        if (buildType != ApplicationBuildSettings.BuildType.Debug) {
            return SignSecrets(
                key = System.getenv("KEY").toString(),
                keyPassword = System.getenv("KEY_PASSWORD").toString(),
                storePassword = System.getenv("STORE_PASSWORD").toString(),
            )
        }

        val secrets = SignSecrets(
            key = props["KEY"].toString().removePrefix("\"").removeSuffix("\""),
            keyPassword = props["KEY_PASSWORD"].toString().removePrefix("\"").removeSuffix("\""),
            storePassword = props["STORE_PASSWORD"].toString().removePrefix("\"")
                .removeSuffix("\""),
        )
        return secrets
    }
    throw FileNotFoundException("Secrets file not found")
}

private fun Project.configureSigning(extension: ApplicationExtension, secrets: SignSecrets) {
    println(secrets)
    extension.signingConfigs {
        getByName(ApplicationBuildSettings.BuildType.Debug.getName()) {
            storeFile = rootProject.file("keystores/debug.p12")
            storePassword = secrets.storePassword
            keyAlias = secrets.key
            keyPassword = secrets.storePassword
        }

//        create(ApplicationBuildSettings.BuildType.Release.getName()) {
//            storeFile = rootProject.file("keystores/keystore.jks")
//            storePassword = secrets.storePassword
//            keyAlias = secrets.key
//            keyPassword = secrets.storePassword
//        }
    }

    extension.buildTypes.getByName(ApplicationBuildSettings.BuildType.Debug.getName()).apply {
        signingConfig = extension.signingConfigs.getByName(
            ApplicationBuildSettings.BuildType.Debug.getName()
        )
    }
//    extension.buildTypes.getByName(ApplicationBuildSettings.BuildType.Release.getName()).apply {
//        signingConfig = extension.signingConfigs.getByName(
//            ApplicationBuildSettings.BuildType.Release.getName()
//        )
//    }
}