plugins {
    alias(libs.plugins.walking.application)
    alias(libs.plugins.walking.kotlin)
    alias(libs.plugins.walking.detekt)
    alias(libs.plugins.ksp)
}

android {
    namespace = "ru.pasha.walking"

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

    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.constraintlayout)

    implementation(libs.dagger)
    ksp(libs.daggerCompiler)

    implementation(libs.yandex.authSdk)

    implementation(libs.cicerone)

    implementation(libs.androidx.securityCrypto)

    implementation(libs.coil)

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    implementation(projects.common)
    implementation(projects.network)
    implementation(projects.coreNavigation)
    implementation(projects.featureBanner)
}