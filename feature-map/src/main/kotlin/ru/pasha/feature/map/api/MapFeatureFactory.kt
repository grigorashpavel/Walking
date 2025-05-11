package ru.pasha.feature.map.api

import ru.pasha.feature.map.internal.di.MapComponent

object MapFeatureFactory {
    fun create(dependencies: MapDependencies): MapFeature =
        MapComponent.create(dependencies).feature
}
