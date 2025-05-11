package ru.pasha.walking.di.features

import androidx.fragment.app.FragmentFactory
import com.github.terrakok.cicerone.Screen
import dagger.Module
import dagger.Provides
import kotlinx.coroutines.flow.Flow
import ru.pasha.common.map.GeoPoint
import ru.pasha.feature.home.api.HomeFeature
import ru.pasha.feature.home.api.HomeFeatureFactory
import ru.pasha.feature.home.api.WalkingMapProvider
import ru.pasha.feature.map.api.MapFeature
import ru.pasha.walking.di.ApplicationComponent

@Module
interface HomeFeatureModule {
    companion object {
        @Provides
        fun provideHomeFeature(withDependenciesComponent: ApplicationComponent): HomeFeature {
            return HomeFeatureFactory.create(withDependenciesComponent)
        }

        @Provides
        fun provideMapProvider(
            mapFeature: MapFeature,
            mapFragmentFactory: FragmentFactory,
        ): WalkingMapProvider {
            return object : WalkingMapProvider {
                override val fragmentFactory: FragmentFactory get() = mapFragmentFactory
                override fun mapFragmentScreen(): Screen = mapFeature.getMapScreen()
                override val pointsProvider: Flow<GeoPoint>
                    get() = mapFeature.mapController.pointsFlow
            }
        }
    }
}
