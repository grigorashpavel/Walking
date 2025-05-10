package ru.pasha.feature.banner.api

interface BannerDependencies

interface BannerUiDependencies {
    val navigateToAuthAction: suspend () -> Unit
}
