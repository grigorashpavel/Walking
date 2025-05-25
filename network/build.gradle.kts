plugins {
    alias(libs.plugins.walking.library)
    alias(libs.plugins.walking.kotlin)
    alias(libs.plugins.walking.detekt)
    kotlin(libs.plugins.serialization.get().pluginId) version libs.plugins.serialization.get().version.displayName
}

android {
    namespace = "ru.pasha.network"

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
    implementation(libs.retrofit)
    implementation(libs.retrofitSerialization)
    implementation(libs.serializationJson)
    implementation(libs.okhttp)
    implementation(libs.okhttpLogging)

    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.kotlinx.coroutines.android)
}