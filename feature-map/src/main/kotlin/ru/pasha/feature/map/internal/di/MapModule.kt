package ru.pasha.feature.map.internal.di

import androidx.fragment.app.Fragment
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap
import ru.pasha.core.navigation.FragmentKey
import ru.pasha.feature.map.api.MapFeature
import ru.pasha.feature.map.internal.MapFeatureImpl
import ru.pasha.feature.map.internal.presentation.MapFragment

@Module
internal interface MapModule {
    @Binds
    fun feature(impl: MapFeatureImpl): MapFeature

    @Binds
    @IntoMap
    @FragmentKey(MapFragment::class)
    fun bindMapFragment(mapFragment: MapFragment): Fragment
}
