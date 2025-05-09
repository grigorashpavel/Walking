package ru.pasha.feature.banner.internal.di

import androidx.fragment.app.Fragment
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap
import ru.pasha.core.navigation.FragmentKey
import ru.pasha.feature.banner.api.BannerFeature
import ru.pasha.feature.banner.internal.BannerFeatureImpl
import ru.pasha.feature.banner.internal.presentation.BannerFragment

@Module
internal interface BannerModule {
    @Binds
    fun feature(impl: BannerFeatureImpl): BannerFeature

    @Binds
    @IntoMap
    @FragmentKey(BannerFragment::class)
    fun bindPreviewFeatureFragment(fragment: BannerFragment): Fragment
}
