@file:Suppress("Filename", "MatchingDeclarationName")

package ru.pasha.walking.di

import javax.inject.Scope

@MustBeDocumented
@Retention(AnnotationRetention.RUNTIME)
annotation class StartScreen

@Scope
@Retention(AnnotationRetention.RUNTIME)
annotation class CommonFeature
