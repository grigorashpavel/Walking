package ru.pasha.feature.settings.api

import com.github.terrakok.cicerone.Router
import ru.pasha.network.api.ApiFactory

interface SettingsDependencies {
    val router: Router
    val apiFactory: ApiFactory
    val settingsNavigationProvider: SettingsNavigationProvider
    val settingsManager: SettingsManager
}

interface SettingsNavigationProvider {
    fun navigateBack()
    fun logout()
}
