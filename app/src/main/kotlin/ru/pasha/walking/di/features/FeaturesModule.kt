package ru.pasha.walking.di.features

import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoSet
import ru.pasha.core.navigation.FragmentInstantiator
import ru.pasha.feature.banner.api.BannerFeature
import ru.pasha.feature.history.api.HistoryFeature
import ru.pasha.feature.home.api.HomeFeature
import ru.pasha.feature.map.api.MapFeature
import ru.pasha.feature.settings.api.SettingsFeature

@Module(includes = [
    BannerFeatureModule::class,
    MapFeatureModule::class,
    HomeFeatureModule::class,
    HistoryFeatureModule::class,
    SettingsFeatureModule::class]
)
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

    @Binds
    @IntoSet
    fun bindHistoryFeature(feature: HistoryFeature): FragmentInstantiator

    @Binds
    @IntoSet
    fun bindSettingsFeature(feature: SettingsFeature): FragmentInstantiator
}
