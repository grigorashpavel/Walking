package ru.pasha.core.navigation

import androidx.fragment.app.Fragment

interface FragmentInstantiator {

    fun instantiate(className: String): Fragment?
}

typealias FragmentInstantiatorSet = Set<@JvmSuppressWildcards FragmentInstantiator>

class FragmentInstantiatorDelegate(
    private val fragmentsMap: FragmentProvidersMap
) : FragmentInstantiator {

    override fun instantiate(className: String): Fragment? {
        return fragmentsMap.entries.firstOrNull { (clazz, _) ->
            clazz.name == className
        }?.value?.get()
    }
}
