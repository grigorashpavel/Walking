package ru.pasha.feature.settings.internal.presentation

import ru.pasha.common.Text
import ru.pasha.feature.settings.internal.domain.Language
import ru.pasha.feature.settings.internal.domain.Theme

internal data class SettingsViewState(
    val language: Language,
    val theme: Theme,
    val locationTrackingEnabled: Boolean,
    val error: ViewError?,
)

internal data class ViewError(val message: Text)
