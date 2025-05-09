package ru.pasha.feature.banner.internal

import com.github.terrakok.cicerone.Screen
import ru.pasha.core.navigation.FragmentScreen
import ru.pasha.feature.banner.internal.presentation.BannerFragment
import javax.inject.Inject

internal class BannerScreenFactory @Inject constructor() {
    fun getBannerScreen(): Screen = FragmentScreen(
        key = "BannerScreen",
        fragmentClass = BannerFragment::class.java
    )
}
