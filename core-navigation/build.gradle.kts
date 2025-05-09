plugins {
    alias(libs.plugins.walking.library)
    alias(libs.plugins.walking.kotlin)
    alias(libs.plugins.walking.detekt)
}

android {
    namespace = "ru.pasha.core.navigation"

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
}

dependencies {
    api(libs.cicerone)
    implementation(libs.androidx.appcompat)
    implementation(libs.dagger)

    implementation(projects.common)
}