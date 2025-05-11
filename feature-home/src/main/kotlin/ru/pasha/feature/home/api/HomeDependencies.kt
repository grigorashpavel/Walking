package ru.pasha.feature.home.api

import androidx.fragment.app.FragmentFactory
import com.github.terrakok.cicerone.Screen
import ru.pasha.common.map.MapController

interface HomeDependencies {
    val mapProvider: WalkingMapProvider
}

interface WalkingMapProvider {
    val fragmentFactory: FragmentFactory
    fun mapFragmentScreen(): Screen
    val mapController: MapController
}
