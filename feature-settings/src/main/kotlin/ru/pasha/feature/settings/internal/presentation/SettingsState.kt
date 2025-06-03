package ru.pasha.feature.settings.internal.presentation

import ru.pasha.feature.settings.internal.domain.Language
import ru.pasha.feature.settings.internal.domain.LocationTracking
import ru.pasha.feature.settings.internal.domain.Theme

internal data class SettingsState(
    val theme: Theme,
    val language: Language,
    val locationTracking: LocationTracking,
)
