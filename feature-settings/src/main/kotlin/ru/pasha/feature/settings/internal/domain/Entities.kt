@file:Suppress("Filename", "MatchingDeclarationName")

package ru.pasha.feature.settings.internal.domain

internal sealed interface SettingsEntity {
    data class Theme(
        val current: ru.pasha.feature.settings.internal.domain.Theme,
    ) : SettingsEntity

    data class Language(
        val current: ru.pasha.feature.settings.internal.domain.Language,
    ) : SettingsEntity

    data class LocationTracking(
        val enabled: Boolean,
    ) : SettingsEntity
}
