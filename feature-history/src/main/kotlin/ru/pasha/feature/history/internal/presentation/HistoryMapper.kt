package ru.pasha.feature.history.internal.presentation

import ru.pasha.common.Text
import ru.pasha.common.pattern.BaseMapper
import ru.pasha.feature.history.internal.domain.PreviewEntity

internal class HistoryMapper : BaseMapper<HistoryState, HistoryViewState>() {
    private var previousRoutes: List<PreviewEntity>? = null
    override fun toViewState(state: HistoryState): HistoryViewState {
        val routes = state.getRoutes()
        previousRoutes = routes

        return HistoryViewState(
            routes = routes,
            error = state.getError(),
            isLoading = state is HistoryState.Loading
        )
    }

    private fun HistoryState.getRoutes(): List<PreviewEntity> = when (this) {
        HistoryState.Loading -> emptyList()
        is HistoryState.Error -> emptyList()
        is HistoryState.Success -> {
            val prev = previousRoutes
            if (prev != null) {
                (prev.toSet() + localRoutes.map {
                    PreviewEntity(
                        it.id,
                        it.name,
                        downloaded = true
                    )
                }.toSet() + remoteRoutes.toSet()).sortedByDescending { it.downloaded }
            } else {
                (localRoutes.map {
                    PreviewEntity(
                        it.id,
                        it.name,
                        downloaded = true
                    )
                }.toSet() + remoteRoutes.toSet()).sortedByDescending { it.downloaded }
            }
        }
    }

    private fun HistoryState.getError(): ViewError? = when (this) {
        HistoryState.Loading -> null
        is HistoryState.Error -> ViewError(
            message = message ?: Text.Constant("Возникла ошиба при загрузке")
        )

        is HistoryState.Success -> null
    }
}
