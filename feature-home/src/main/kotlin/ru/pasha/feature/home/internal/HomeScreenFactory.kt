package ru.pasha.feature.home.internal

import com.github.terrakok.cicerone.Screen
import ru.pasha.common.pattern.ScreenArguments
import ru.pasha.core.navigation.FragmentScreen
import ru.pasha.feature.home.internal.presentation.HomeFragment
import javax.inject.Inject

internal class HomeScreenFactory @Inject constructor() {
    fun getHomeScreen(screenArguments: ScreenArguments): Screen = FragmentScreen(
        key = "HomeScreen",
        fragmentClass = HomeFragment::class.java,
        screenArguments = screenArguments,
    )
}
