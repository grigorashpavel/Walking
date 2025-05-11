package ru.pasha.feature.map.internal

import com.github.terrakok.cicerone.Screen
import ru.pasha.core.navigation.FragmentScreen
import ru.pasha.feature.map.internal.presentation.MapFragment
import javax.inject.Inject

internal class MapScreenFactory @Inject constructor() {
    fun getMapScreen(): Screen = FragmentScreen(
        key = "MapScreen",
        fragmentClass = MapFragment::class.java
    )
}
