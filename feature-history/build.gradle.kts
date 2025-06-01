plugins {
    alias(libs.plugins.walking.library)
    alias(libs.plugins.walking.kotlin)
    alias(libs.plugins.walking.detekt)
    alias(libs.plugins.ksp)
    kotlin(libs.plugins.serialization.get().pluginId) version libs.plugins.serialization.get().version.displayName
}

android {
    namespace = "ru.pasha.feature.history"

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
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)

    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.kotlinx.coroutines.android)

    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.lifecycle.viewmodel.ktx)

    implementation(libs.okhttp)
    implementation(libs.retrofit)
    implementation(libs.serializationJson)

    implementation(libs.dagger)
    ksp(libs.daggerCompiler)

    implementation(projects.common)
    implementation(projects.coreNavigation)
    implementation(projects.network)
}