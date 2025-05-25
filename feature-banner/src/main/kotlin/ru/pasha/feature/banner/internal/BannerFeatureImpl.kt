package ru.pasha.feature.banner.internal

import com.github.terrakok.cicerone.Screen
import ru.pasha.core.navigation.FragmentInstantiator
import ru.pasha.core.navigation.FragmentInstantiatorDelegate
import ru.pasha.core.navigation.FragmentProvidersMap
import ru.pasha.feature.banner.api.BannerFeature
import javax.inject.Inject

internal class BannerFeatureImpl @Inject constructor(
    private val screenFactory: BannerScreenFactory,
    fragmentsMap: FragmentProvidersMap
) : BannerFeature, FragmentInstantiator by FragmentInstantiatorDelegate(fragmentsMap) {
    override fun getBannerScreen(): Screen = screenFactory.getBannerScreen()
}
