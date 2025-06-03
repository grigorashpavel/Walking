package ru.pasha.feature.home.internal.di

import androidx.fragment.app.Fragment
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap
import ru.pasha.core.navigation.FragmentKey
import ru.pasha.feature.home.api.HomeFeature
import ru.pasha.feature.home.internal.HomeFeatureImpl
import ru.pasha.feature.home.internal.data.HomeApi
import ru.pasha.feature.home.internal.presentation.HomeFragment
import ru.pasha.network.api.ApiFactory

@Module
internal interface HomeModule {
    @Binds
    fun feature(impl: HomeFeatureImpl): HomeFeature

    @Binds
    @IntoMap
    @FragmentKey(HomeFragment::class)
    fun bindHomeFragment(homeFragment: HomeFragment): Fragment

    companion object {
        @Provides
        @HomeScope
        fun provideHomeApi(apiFactory: ApiFactory) = apiFactory.create<HomeApi>()
    }
}
