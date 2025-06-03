package ru.pasha.feature.home.api

import ru.pasha.common.di.WalkingMapProvider

interface HomeDependencies {
    val mapProvider: WalkingMapProvider
    val homeNavigationProvider: HomeNavigationProvider
    val locationTrackerSettingProvider: LocationTrackerSettingProvider
}

interface LocationTrackerSettingProvider {
    val isEnabled: Boolean
}

interface HomeNavigationProvider {
    fun navigateToHistory()
    fun navigateToSettings()
}
