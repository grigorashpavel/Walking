package ru.pasha.core.navigation

import com.github.terrakok.cicerone.Router
import com.github.terrakok.cicerone.androidx.AppNavigator

interface NavigationApi {
    fun getRouter(): Router
    fun setNavigator(navigator: AppNavigator)
    fun removeNavigator()
}
