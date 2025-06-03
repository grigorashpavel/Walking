package ru.pasha.feature.settings.api

import ru.pasha.feature.settings.internal.di.SettingsComponent

object SettingsFeatureFactory {
    fun create(dependencies: SettingsDependencies): SettingsFeature =
        SettingsComponent.create(dependencies).feature
}
