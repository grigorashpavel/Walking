package ru.pasha.walking.di.features

import dagger.Module
import dagger.Provides
import ru.pasha.feature.history.api.HistoryFeature
import ru.pasha.feature.history.api.HistoryFeatureFactory
import ru.pasha.walking.di.ApplicationComponent

@Module
interface HistoryFeatureModule {
    companion object {
        @Provides
        fun provideHistoryFeature(withDependenciesComponent: ApplicationComponent): HistoryFeature {
            return HistoryFeatureFactory.create(withDependenciesComponent)
        }
    }
}
