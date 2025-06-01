package ru.pasha.feature.history.internal.di

import dagger.Component
import ru.pasha.feature.history.api.HistoryDependencies
import ru.pasha.feature.history.api.HistoryFeature

@Component(dependencies = [HistoryDependencies::class], modules = [HistoryModule::class])
@HistoryScope
interface HistoryComponent {
    val feature: HistoryFeature

    companion object {
        fun create(dependencies: HistoryDependencies): HistoryComponent =
            DaggerHistoryComponent.factory().create(dependencies)
    }

    @Component.Factory
    interface Factory {
        fun create(dependencies: HistoryDependencies): HistoryComponent
    }
}
