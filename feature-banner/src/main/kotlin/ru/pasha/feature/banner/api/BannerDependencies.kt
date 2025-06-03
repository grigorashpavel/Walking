package ru.pasha.feature.banner.api

import kotlinx.coroutines.flow.Flow

interface BannerDependencies {
    val connectionProvider: ConnectionProvider
    val bannerNavigationProvider: BannerNavigationProvider
}

interface BannerNavigationProvider {
    val navigateToHomeAction: () -> Unit
}

interface ConnectionProvider {
    val hasConnection: Boolean
}

interface BannerUiDependencies {
    val navigateToAuthAction: suspend () -> Flow<Boolean>
}
