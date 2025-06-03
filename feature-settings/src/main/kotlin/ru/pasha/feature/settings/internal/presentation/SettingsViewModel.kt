package ru.pasha.feature.settings.internal.presentation

import android.util.Log
import androidx.lifecycle.viewModelScope
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.launch
import ru.pasha.common.Text
import ru.pasha.common.pattern.BaseViewModel
import ru.pasha.common.pattern.SideEffect
import ru.pasha.feature.settings.api.SettingsManager
import ru.pasha.feature.settings.api.SettingsNavigationProvider
import ru.pasha.feature.settings.internal.data.SettingsRepository
import ru.pasha.feature.settings.internal.domain.Language
import ru.pasha.feature.settings.internal.domain.LocationTracking
import ru.pasha.feature.settings.internal.domain.Theme

internal class SettingsViewModel @AssistedInject constructor(
    private val repository: SettingsRepository,
    private val settingsNavigationProvider: SettingsNavigationProvider,
    private val settingsManager: SettingsManager,
) : BaseViewModel<SettingsState, SettingsViewState>(
    mapper = SettingsMapper(),
    initialState = SettingsState(
        theme = settingsManager.appTheme,
        language = settingsManager.appLanguage,
        locationTracking = settingsManager.locationTrackingEnabled,
    )
) {
    fun navigateBack() = settingsNavigationProvider.navigateBack()

    fun changeLanguage(newLanguage: Language) {
        settingsManager.appLanguage = newLanguage
        updateState { copy(language = newLanguage) }
    }

    fun changeTheme(newTheme: Theme) {
        settingsManager.appTheme = newTheme
        updateState { copy(theme = newTheme) }
    }

    fun changeLocationOption(newOption: LocationTracking) {
        settingsManager.locationTrackingEnabled = newOption
        updateState { copy(locationTracking = newOption) }
    }

    fun logout() {
        viewModelScope.launch { repository.endSession() }
        settingsNavigationProvider.logout()
    }

    fun reportProblem(message: String) {
        viewModelScope.launch {
            val res = repository.reportProblem(message)
            Log.d("===", res.toString())
        }
    }

    @AssistedFactory
    interface Factory {
        fun create(): SettingsViewModel
    }
}

data class DownloadError(val title: Text) : SideEffect
