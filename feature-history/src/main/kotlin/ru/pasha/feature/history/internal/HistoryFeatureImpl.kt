package ru.pasha.feature.history.internal

import com.github.terrakok.cicerone.Screen
import ru.pasha.core.navigation.FragmentInstantiator
import ru.pasha.core.navigation.FragmentInstantiatorDelegate
import ru.pasha.core.navigation.FragmentProvidersMap
import ru.pasha.feature.history.api.HistoryFeature
import ru.pasha.feature.history.internal.di.HistoryScope
import javax.inject.Inject

@HistoryScope
internal class HistoryFeatureImpl @Inject constructor(
    private val screenFactory: HistoryScreenFactory,
    fragmentMap: FragmentProvidersMap,
) : HistoryFeature, FragmentInstantiator by FragmentInstantiatorDelegate(fragmentMap) {
    override fun getHistoryScreen(): Screen = screenFactory.getHistoryScreen()
}
