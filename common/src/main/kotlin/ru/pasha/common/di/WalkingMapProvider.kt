package ru.pasha.common.di

import androidx.fragment.app.FragmentFactory
import com.github.terrakok.cicerone.Screen
import ru.pasha.common.map.MapController

interface WalkingMapProvider {
    val fragmentFactory: FragmentFactory
    fun mapFragmentScreen(): Screen
    val mapController: MapController
}
