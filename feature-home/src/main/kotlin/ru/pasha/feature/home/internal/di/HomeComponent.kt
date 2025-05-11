package ru.pasha.feature.home.internal.di

import dagger.Component
import ru.pasha.feature.home.api.HomeDependencies
import ru.pasha.feature.home.api.HomeFeature

@Component(dependencies = [HomeDependencies::class], modules = [HomeModule::class])
@HomeScope
interface HomeComponent {
    val feature: HomeFeature

    companion object {
        fun create(dependencies: HomeDependencies): HomeComponent =
            DaggerHomeComponent.factory().create(dependencies)
    }

    @Component.Factory
    interface Factory {
        fun create(dependencies: HomeDependencies): HomeComponent
    }
}
