package ru.pasha.feature.settings.internal.di

import androidx.fragment.app.Fragment
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap
import ru.pasha.core.navigation.FragmentKey
import ru.pasha.feature.settings.api.SettingsFeature
import ru.pasha.feature.settings.internal.SettingsFeatureImpl
import ru.pasha.feature.settings.internal.data.SettingsApi
import ru.pasha.feature.settings.internal.presentation.SettingsFragment
import ru.pasha.network.api.ApiFactory

@Module
internal interface SettingsModule {
    @Binds
    fun feature(impl: SettingsFeatureImpl): SettingsFeature

    @Binds
    @IntoMap
    @FragmentKey(SettingsFragment::class)
    fun bindHomeFragment(homeFragment: SettingsFragment): Fragment

    companion object {
        @Provides
        @SettingsScope
        fun provideHistoryApi(apiFactory: ApiFactory): SettingsApi = apiFactory.create<SettingsApi>()
    }
}
