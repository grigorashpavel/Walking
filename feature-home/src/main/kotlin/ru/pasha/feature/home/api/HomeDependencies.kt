package ru.pasha.feature.home.api

import ru.pasha.common.di.WalkingMapProvider
import ru.pasha.network.api.ApiFactory

interface HomeDependencies {
    val mapProvider: WalkingMapProvider
    val homeNavigationProvider: HomeNavigationProvider
    val locationTrackerSettingProvider: LocationTrackerSettingProvider
    val apiFactory: ApiFactory
}

interface LocationTrackerSettingProvider {
    val isEnabled: Boolean
}

interface HomeNavigationProvider {
    fun navigateToHistory()
    fun navigateToSettings()
}
