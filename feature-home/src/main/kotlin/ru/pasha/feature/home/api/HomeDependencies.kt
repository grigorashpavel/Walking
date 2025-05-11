package ru.pasha.feature.home.api

import androidx.fragment.app.FragmentFactory
import com.github.terrakok.cicerone.Screen
import kotlinx.coroutines.flow.Flow
import ru.pasha.common.map.GeoPoint

interface HomeDependencies {
    val mapProvider: WalkingMapProvider
}

interface WalkingMapProvider {
    val fragmentFactory: FragmentFactory
    fun mapFragmentScreen(): Screen
    val pointsProvider: Flow<GeoPoint>
}
