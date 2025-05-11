package ru.pasha.walking.di.features

import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoSet
import ru.pasha.core.navigation.FragmentInstantiator
import ru.pasha.feature.banner.api.BannerFeature
import ru.pasha.feature.home.api.HomeFeature
import ru.pasha.feature.map.api.MapFeature

@Module(includes = [BannerFeatureModule::class, MapFeatureModule::class, HomeFeatureModule::class])
interface FeaturesModule {
    @Binds
    @IntoSet
    fun bindBannerFeature(feature: BannerFeature): FragmentInstantiator

    @Binds
    @IntoSet
    fun bindMapFeature(feature: MapFeature): FragmentInstantiator

    @Binds
    @IntoSet
    fun bindHomeFeature(feature: HomeFeature): FragmentInstantiator
}
