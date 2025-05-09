package ru.pasha.core.navigation

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentFactory
import javax.inject.Inject

class ScreenFactory @Inject constructor(
    private val instantiators: FragmentInstantiatorSet,
) : FragmentFactory() {

    override fun instantiate(classLoader: ClassLoader, className: String): Fragment {
        return instantiators.firstNotNullOfOrNull { instantiator ->
            instantiator.instantiate(className)
        } ?: super.instantiate(classLoader, className)
    }
}
