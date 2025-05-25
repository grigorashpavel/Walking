package ru.pasha.core.navigation

import androidx.fragment.app.FragmentFactory
import com.github.terrakok.cicerone.Cicerone
import com.github.terrakok.cicerone.Router
import com.github.terrakok.cicerone.Screen

interface NavigationHolder {
    val factory: FragmentFactory
    val startScreen: Screen

    val cicerone: Cicerone<Router>
    val router: Router
}
