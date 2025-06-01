package ru.pasha.feature.history.internal

import com.github.terrakok.cicerone.Screen
import ru.pasha.core.navigation.FragmentScreen
import ru.pasha.feature.history.internal.presentation.HistoryFragment
import javax.inject.Inject

internal class HistoryScreenFactory @Inject constructor() {
    fun getHistoryScreen(): Screen = FragmentScreen(
        key = "HistoryScreen",
        fragmentClass = HistoryFragment::class.java,
    )
}
