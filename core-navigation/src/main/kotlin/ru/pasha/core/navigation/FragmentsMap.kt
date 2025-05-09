@file:Suppress("MatchingDeclarationName")

package ru.pasha.core.navigation

import androidx.fragment.app.Fragment
import dagger.MapKey
import javax.inject.Provider
import kotlin.reflect.KClass

@MapKey
@Target(AnnotationTarget.FUNCTION)
annotation class FragmentKey(val value: KClass<out Fragment>)

typealias FragmentProvidersMap = Map<Class<out Fragment>, @JvmSuppressWildcards Provider<Fragment>>
