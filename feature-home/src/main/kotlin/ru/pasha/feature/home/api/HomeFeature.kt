package ru.pasha.feature.home.api

import com.github.terrakok.cicerone.Screen
import ru.pasha.core.navigation.FragmentInstantiator

interface HomeFeature : FragmentInstantiator {
    fun getHomeScreen(): Screen
}
