package ru.pasha.feature.history.api

import ru.pasha.feature.history.internal.di.HistoryComponent

object HistoryFeatureFactory {
    fun create(dependencies: HistoryDependencies): HistoryFeature =
        HistoryComponent.create(dependencies).feature
}
