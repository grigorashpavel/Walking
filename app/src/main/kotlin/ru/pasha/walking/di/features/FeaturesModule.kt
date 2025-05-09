package ru.pasha.walking.di.features

import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoSet
import ru.pasha.core.navigation.FragmentInstantiator
import ru.pasha.feature.banner.api.BannerFeature

@Module(includes = [BannerFeatureModule::class])
interface FeaturesModule {
    @Binds
    @IntoSet
    fun bindStartPreviewFeature(feature: BannerFeature): FragmentInstantiator
}
