package ru.pasha.walking.di.features

import dagger.Module
import dagger.Provides
import ru.pasha.feature.map.api.MapFeature
import ru.pasha.feature.map.api.MapFeatureFactory
import ru.pasha.walking.di.ApplicationComponent
import ru.pasha.walking.di.CommonFeature

@Module
interface MapFeatureModule {
    companion object {
        @Provides
        @CommonFeature
        fun provideMapFeature(withDependenciesComponent: ApplicationComponent): MapFeature {
            return MapFeatureFactory.create(withDependenciesComponent)
        }
    }
}
