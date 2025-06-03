package ru.pasha.feature.settings.internal.di

import dagger.Component
import ru.pasha.feature.settings.api.SettingsDependencies
import ru.pasha.feature.settings.api.SettingsFeature

@Component(dependencies = [SettingsDependencies::class], modules = [SettingsModule::class])
@SettingsScope
internal interface SettingsComponent {
    val feature: SettingsFeature

    companion object {
        fun create(dependencies: SettingsDependencies): SettingsComponent =
            DaggerSettingsComponent.factory().create(dependencies)
    }

    @Component.Factory
    interface Factory {
        fun create(dependencies: SettingsDependencies): SettingsComponent
    }
}
