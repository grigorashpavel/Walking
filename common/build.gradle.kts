plugins {
    alias(libs.plugins.walking.library)
    alias(libs.plugins.walking.kotlin)
    alias(libs.plugins.walking.detekt)
}

android {
    namespace = "ru.pasha.common"

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

    implementation(libs.cicerone)

    implementation(libs.coil)

    implementation(libs.dagger)
}