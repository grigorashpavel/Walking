package ru.pasha.feature.history.internal.di

import androidx.fragment.app.Fragment
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap
import ru.pasha.core.navigation.FragmentKey
import ru.pasha.feature.history.api.HistoryFeature
import ru.pasha.feature.history.internal.HistoryFeatureImpl
import ru.pasha.feature.history.internal.data.HistoryApi
import ru.pasha.feature.history.internal.presentation.HistoryFragment
import ru.pasha.network.api.ApiFactory

@Module
internal interface HistoryModule {
    @Binds
    fun feature(impl: HistoryFeatureImpl): HistoryFeature

    @Binds
    @IntoMap
    @FragmentKey(HistoryFragment::class)
    fun bindHomeFragment(homeFragment: HistoryFragment): Fragment

    companion object {
        @Provides
        @HistoryScope
        fun provideHistoryApi(apiFactory: ApiFactory): HistoryApi = apiFactory.create<HistoryApi>()
    }
}
