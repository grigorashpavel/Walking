package ru.pasha.feature.map.api

import com.github.terrakok.cicerone.Screen
import ru.pasha.common.map.MapController
import ru.pasha.core.navigation.FragmentInstantiator

interface MapFeature : FragmentInstantiator {
    fun getMapScreen(): Screen
    val mapController: MapController
}
