package ru.pasha.feature.settings.internal

import com.github.terrakok.cicerone.Screen
import ru.pasha.core.navigation.FragmentScreen
import ru.pasha.feature.settings.internal.presentation.SettingsFragment
import javax.inject.Inject

internal class SettingsScreenFactory @Inject constructor() {
    fun getSettingsScreen(): Screen = FragmentScreen(
        key = "SettingsScreen",
        fragmentClass = SettingsFragment::class.java,
    )
}
