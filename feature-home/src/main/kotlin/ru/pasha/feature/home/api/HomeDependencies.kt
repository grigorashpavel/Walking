package ru.pasha.feature.home.api

import ru.pasha.common.di.WalkingMapProvider

interface HomeDependencies {
    val mapProvider: WalkingMapProvider
    val homeNavigationProvider: HomeNavigationProvider
}

interface HomeNavigationProvider {
    fun navigateToHistory()
}
