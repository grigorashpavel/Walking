package ru.pasha.feature.home.internal

import com.github.terrakok.cicerone.Screen
import ru.pasha.core.navigation.FragmentInstantiator
import ru.pasha.core.navigation.FragmentInstantiatorDelegate
import ru.pasha.core.navigation.FragmentProvidersMap
import ru.pasha.feature.home.api.HomeArguments
import ru.pasha.feature.home.api.HomeFeature
import ru.pasha.feature.home.internal.di.HomeScope
import javax.inject.Inject

@HomeScope
internal class HomeFeatureImpl @Inject constructor(
    private val screenFactory: HomeScreenFactory,
    fragmentMap: FragmentProvidersMap,
) : HomeFeature, FragmentInstantiator by FragmentInstantiatorDelegate(fragmentMap) {
    override fun getHomeScreen(args: HomeArguments): Screen = screenFactory.getHomeScreen(args)
}
