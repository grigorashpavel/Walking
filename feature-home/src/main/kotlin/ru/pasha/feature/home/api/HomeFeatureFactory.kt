package ru.pasha.feature.home.api

import ru.pasha.feature.home.internal.di.HomeComponent

object HomeFeatureFactory {
    fun create(dependencies: HomeDependencies): HomeFeature =
        HomeComponent.create(dependencies).feature
}
