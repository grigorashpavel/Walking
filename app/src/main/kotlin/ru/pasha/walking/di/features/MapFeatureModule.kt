package ru.pasha.walking.di.features

import android.content.Context
import dagger.Module
import dagger.Provides
import ru.pasha.common.R
import ru.pasha.common.map.GeoPoint
import ru.pasha.feature.map.api.MapFeature
import ru.pasha.feature.map.api.MapFeatureFactory
import ru.pasha.feature.map.api.MapSettingsProvider
import ru.pasha.walking.di.ApplicationComponent
import ru.pasha.walking.di.CommonFeature

@Module
interface MapFeatureModule {
    companion object {
        @Provides
        @CommonFeature
        fun provideMapFeature(withDependenciesComponent: ApplicationComponent): MapFeature {
            return MapFeatureFactory.create(withDependenciesComponent)
        }

        @Provides
        fun provideSettings(context: Context): MapSettingsProvider {
            return object : MapSettingsProvider {
                override val startLocation: GeoPoint = GeoPoint(lat = 55.544998f, lon = 37.073382f)
                override val startZoom: Double = 15.0
                override val isCenterMarkerVisible: Boolean = false
                override val maxPois: Int = 6
                override val markersColor: List<Int> = with(context.resources) {
                    listOf(
                        getColor(R.color.walking_app_marker_1, context.theme),
                        getColor(R.color.walking_app_marker_2, context.theme),
                        getColor(R.color.walking_app_marker_3, context.theme),
                        getColor(R.color.walking_app_marker_4, context.theme),
                        getColor(R.color.walking_app_marker_5, context.theme),
                        getColor(R.color.walking_app_marker_6, context.theme),
                    )
                }
            }
        }
    }
}
