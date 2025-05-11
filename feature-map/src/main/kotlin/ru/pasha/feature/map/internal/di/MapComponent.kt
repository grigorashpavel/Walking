package ru.pasha.feature.map.internal.di

import dagger.Component
import ru.pasha.feature.map.api.MapDependencies
import ru.pasha.feature.map.api.MapFeature

@MapScope
@Component(dependencies = [MapDependencies::class], modules = [MapModule::class])
interface MapComponent {
    val feature: MapFeature

    companion object {
        fun create(dependencies: MapDependencies): MapComponent =
            DaggerMapComponent.factory().create(dependencies)
    }

    @Component.Factory
    interface Factory {
        fun create(dependencies: MapDependencies): MapComponent
    }
}
