package ru.pasha.feature.settings.internal.presentation

import ru.pasha.common.pattern.BaseMapper

internal class SettingsMapper : BaseMapper<SettingsState, SettingsViewState>() {
    override fun toViewState(state: SettingsState): SettingsViewState {
        return SettingsViewState(
            theme = state.theme,
            language = state.language,
            locationTrackingEnabled = state.locationTracking.value,
            error = null,
        )
    }
}
