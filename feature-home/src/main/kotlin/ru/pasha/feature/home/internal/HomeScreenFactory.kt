package ru.pasha.feature.home.internal

import com.github.terrakok.cicerone.Screen
import ru.pasha.core.navigation.FragmentScreen
import ru.pasha.feature.home.internal.presentation.HomeFragment
import javax.inject.Inject

internal class HomeScreenFactory @Inject constructor() {
    fun getHomeScreen(): Screen = FragmentScreen(
        key = "HomeScreen",
        fragmentClass = HomeFragment::class.java
    )
}
