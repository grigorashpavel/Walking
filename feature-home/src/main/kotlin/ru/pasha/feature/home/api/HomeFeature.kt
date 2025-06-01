package ru.pasha.feature.home.api

import com.github.terrakok.cicerone.Screen
import kotlinx.parcelize.Parcelize
import ru.pasha.common.map.Route
import ru.pasha.common.pattern.ScreenArguments
import ru.pasha.core.navigation.FragmentInstantiator

interface HomeFeature : FragmentInstantiator {
    fun getHomeScreen(args: HomeArguments): Screen
}

@Parcelize
data class HomeArguments(val route: Route?) : ScreenArguments
