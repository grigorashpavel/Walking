plugins {
    `kotlin-dsl`
}

group = "ru.pasha.walking.buildlogic"

dependencies {
    compileOnly(libs.kotlin.gradlePlugin)
    compileOnly(libs.android.gradlePlugin)
    compileOnly(libs.detekt.cli)
    compileOnly(libs.detekt.formatting)
    compileOnly(libs.detekt.gradlePlugin)
    compileOnly(files(libs.javaClass.superclass.protectionDomain.codeSource.location.path))
}

gradlePlugin {
    plugins {
        register(ApplicationPlugin.name) {
            val plugin = libs.plugins.walking.application.get()
            id = plugin.pluginId
            version = plugin.version
            implementationClass = ApplicationPlugin.implementationClass
        }
        register(LibraryPlugin.name) {
            val plugin = libs.plugins.walking.library.get()
            id = plugin.pluginId
            version = plugin.version
            implementationClass = LibraryPlugin.implementationClass
        }
        register(AndroidKotlinPlugin.name) {
            val plugin = libs.plugins.walking.kotlin.get()
            id = plugin.pluginId
            version = plugin.version
            implementationClass = AndroidKotlinPlugin.implementationClass
        }
        register(DetektPlugin.name) {
            val plugin = libs.plugins.walking.detekt.get()
            id = plugin.pluginId
            version = plugin.version
            implementationClass = DetektPlugin.implementationClass
        }
    }
}

private object ApplicationPlugin {
    const val name: String = "WalkingAndroidApplicationPlugin"
    const val implementationClass: String = "AndroidApplicationConventionPlugin"
}

private object LibraryPlugin {
    const val name: String = "WalkingAndroidLibraryPlugin"
    const val implementationClass: String = "AndroidLibraryConventionPlugin"
}

private object AndroidKotlinPlugin {
    const val name: String = "WalkingAndroidKotlinPlugin"
    const val implementationClass: String = "AndroidKotlinConventionPlugin"
}

private object DetektPlugin {
    const val name: String = "WalkingDetektPlugin"
    const val implementationClass: String = "DetektConventionPlugin"
}
