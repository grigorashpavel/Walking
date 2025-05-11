package ru.pasha.walking.di.features

import dagger.Module
import dagger.Provides
import ru.pasha.feature.banner.api.BannerFeature
import ru.pasha.feature.banner.api.BannerFeatureFactory
import ru.pasha.walking.di.ApplicationComponent

@Module
interface BannerFeatureModule {
    companion object {
        @Provides
        fun provideBannerFeature(withDependenciesComponent: ApplicationComponent): BannerFeature {
            return BannerFeatureFactory.create(withDependenciesComponent)
        }
    }
}
