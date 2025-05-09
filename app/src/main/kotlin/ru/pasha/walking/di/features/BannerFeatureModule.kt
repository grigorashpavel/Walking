package ru.pasha.walking.di.features

import dagger.Module
import dagger.Provides
import ru.pasha.feature.banner.api.BannerFeature
import ru.pasha.feature.banner.api.BannerFeatureFactory
import ru.pasha.walking.di.ActivityComponent

@Module
interface BannerFeatureModule {
    companion object {
        @Provides
        fun provideBannerFeature(withDependenciesComponent: ActivityComponent): BannerFeature {
            return BannerFeatureFactory.create(withDependenciesComponent)
        }
    }
}
