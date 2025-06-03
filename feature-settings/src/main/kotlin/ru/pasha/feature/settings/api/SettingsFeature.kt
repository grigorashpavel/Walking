package ru.pasha.feature.settings.api

import com.github.terrakok.cicerone.Screen
import ru.pasha.core.navigation.FragmentInstantiator

interface SettingsFeature : FragmentInstantiator {
    fun getSettingsScreen(): Screen
}
