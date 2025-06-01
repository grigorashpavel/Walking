package ru.pasha.feature.history.internal.presentation

import ru.pasha.common.Text
import ru.pasha.common.pattern.BaseMapper
import ru.pasha.feature.history.internal.domain.PreviewEntity

class HistoryMapper : BaseMapper<HistoryState, HistoryViewState>() {
    override fun toViewState(state: HistoryState): HistoryViewState {
        return HistoryViewState(
            routes = state.getRoutes(),
            error = state.getError(),
            isLoading = state is HistoryState.Loading
        )
    }

    private fun HistoryState.getRoutes(): List<PreviewEntity> = when (this) {
        HistoryState.Loading -> emptyList()
        is HistoryState.Error -> emptyList()
        is HistoryState.Success -> (localRoutes.map {
            PreviewEntity(
                it.id,
                it.name,
                downloaded = true
            )
        }.toSet() + remoteRoutes.toSet()).sortedByDescending { it.downloaded }
    }

    private fun HistoryState.getError(): ViewError? = when (this) {
        HistoryState.Loading -> null
        is HistoryState.Error -> ViewError(
            message = message ?: Text.Constant("Возникла ошиба при загрузке")
        )

        is HistoryState.Success -> null
    }
}
