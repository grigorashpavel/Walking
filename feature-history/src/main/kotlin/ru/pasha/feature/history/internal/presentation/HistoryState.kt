package ru.pasha.feature.history.internal.presentation

import ru.pasha.common.Text
import ru.pasha.common.map.Route
import ru.pasha.feature.history.internal.domain.PreviewEntity

sealed interface HistoryState {
    data object Loading : HistoryState
    data class Error(val message: Text?) : HistoryState
    data class Success(val remoteRoutes: List<PreviewEntity>, val localRoutes: List<Route>) : HistoryState
}
