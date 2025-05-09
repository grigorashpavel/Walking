package ru.pasha.feature.banner.api

import com.github.terrakok.cicerone.Screen
import ru.pasha.core.navigation.FragmentInstantiator

interface BannerFeature : FragmentInstantiator {
    fun getFeaturePreviewScreen(): Screen
}
