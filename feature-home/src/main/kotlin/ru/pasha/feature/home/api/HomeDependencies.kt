package ru.pasha.feature.home.api

import android.content.Context
import ru.pasha.common.di.WalkingMapProvider
import ru.pasha.network.api.ApiFactory

interface HomeDependencies {
    val mapProvider: WalkingMapProvider
    val homeNavigationProvider: HomeNavigationProvider
    val settingsProvider: SettingsProvider
    val apiFactory: ApiFactory
    val context: Context
}

interface SettingsProvider {
    val locationTrackingEnabled: Boolean
    val stepsTrackingEnabled: Boolean
}

interface HomeNavigationProvider {
    fun navigateToHistory()
    fun navigateToSettings()
}
