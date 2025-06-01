package ru.pasha.walking.di.features

import dagger.Module
import dagger.Provides
import ru.pasha.feature.home.api.HomeFeature
import ru.pasha.feature.home.api.HomeFeatureFactory
import ru.pasha.walking.di.ApplicationComponent
import ru.pasha.walking.di.CommonFeature

@Module
interface HomeFeatureModule {
    companion object {
        @Provides
        @CommonFeature
        fun provideHomeFeature(withDependenciesComponent: ApplicationComponent): HomeFeature {
            return HomeFeatureFactory.create(withDependenciesComponent)
        }
    }
}
