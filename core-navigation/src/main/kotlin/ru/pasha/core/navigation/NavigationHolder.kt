package ru.pasha.core.navigation

import androidx.fragment.app.FragmentFactory
import com.github.terrakok.cicerone.Screen

interface NavigationHolder {
    val factory: FragmentFactory
    val startScreen: Screen
}
