package ru.pasha.feature.history.api

import com.github.terrakok.cicerone.Screen
import ru.pasha.core.navigation.FragmentInstantiator

interface HistoryFeature : FragmentInstantiator {
    fun getHistoryScreen(): Screen
}
