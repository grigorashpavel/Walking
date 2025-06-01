package ru.pasha.feature.history.internal.presentation

import ru.pasha.common.Text
import ru.pasha.feature.history.internal.domain.PreviewEntity

internal data class HistoryViewState(val routes: List<PreviewEntity>, val error: ViewError?, val isLoading: Boolean)

internal data class ViewError(val message: Text)
