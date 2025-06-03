package ru.pasha.feature.settings.internal

import com.github.terrakok.cicerone.Screen
import ru.pasha.core.navigation.FragmentInstantiator
import ru.pasha.core.navigation.FragmentInstantiatorDelegate
import ru.pasha.core.navigation.FragmentProvidersMap
import ru.pasha.feature.settings.api.SettingsFeature
import ru.pasha.feature.settings.internal.di.SettingsScope
import javax.inject.Inject

@SettingsScope
internal class SettingsFeatureImpl @Inject constructor(
    private val screenFactory: SettingsScreenFactory,
    fragmentMap: FragmentProvidersMap,
) : SettingsFeature, FragmentInstantiator by FragmentInstantiatorDelegate(fragmentMap) {
    override fun getSettingsScreen(): Screen = screenFactory.getSettingsScreen()
}
