package ru.pasha.feature.map.internal

import com.github.terrakok.cicerone.Screen
import ru.pasha.common.map.MapController
import ru.pasha.core.navigation.FragmentInstantiator
import ru.pasha.core.navigation.FragmentInstantiatorDelegate
import ru.pasha.core.navigation.FragmentProvidersMap
import ru.pasha.feature.map.api.MapFeature
import ru.pasha.feature.map.internal.di.MapScope
import javax.inject.Inject

@MapScope
internal class MapFeatureImpl @Inject constructor(
    private val screenFactory: MapScreenFactory,
    mapControllerProvider: MapControllerProvider,
    fragmentMap: FragmentProvidersMap,
) : MapFeature, FragmentInstantiator by FragmentInstantiatorDelegate(fragmentMap) {
    override fun getMapScreen(): Screen = screenFactory.getMapScreen()

    override val mapController: MapController = mapControllerProvider
}
