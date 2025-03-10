package convention

import org.gradle.api.JavaVersion

object ApplicationBuildSettings {
    const val aaplicationId = "ru.pasha.walking"
    const val compileSdk = 34
    const val targetSdk = 34
    const val minSdk = 26
    const val versionCode = 1
    const val versionName = "0.0.1-a"
    val javaVersion = JavaVersion.VERSION_1_8
    val jvmTarget = JavaVersion.VERSION_1_8.toString()

    enum class BuildType {
        Debug, Dev, Prod;

        fun getName() = this.name.lowercase()
    }
}