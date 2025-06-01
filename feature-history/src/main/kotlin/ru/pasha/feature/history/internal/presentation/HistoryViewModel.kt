@file:Suppress("CyclomaticComplexMethod")

package ru.pasha.feature.history.internal.presentation

import androidx.lifecycle.viewModelScope
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withTimeoutOrNull
import ru.pasha.common.Text
import ru.pasha.common.pattern.BaseViewModel
import ru.pasha.common.pattern.SideEffect
import ru.pasha.feature.history.api.NavigationProvider
import ru.pasha.feature.history.internal.data.HistoryRepository
import ru.pasha.feature.history.internal.data.entity
import ru.pasha.feature.history.internal.domain.PreviewEntity

internal class HistoryViewModel @AssistedInject constructor(
    private val repository: HistoryRepository,
    private val navigationProvider: NavigationProvider,
) : BaseViewModel<HistoryState, HistoryViewState>(
    mapper = HistoryMapper(),
    initialState = HistoryState.Loading
) {
    private var loadJob: Job? = null
    private var downloadJob: Job? = null
    private var removeJob: Job? = null
    private var navigationJob: Job? = null

    private var pageIndex = 0

    init {
        loadRoutes()
    }

    fun loadRoutes() {
        loadJob?.cancel()
        updateState { HistoryState.Loading }
        loadJob = viewModelScope.launch {
            repository.getSavedPreviewRoutes()?.let {
                updateState {
                    when (this) {
                        is HistoryState.Error -> HistoryState.Success(
                            localRoutes = it,
                            remoteRoutes = emptyList()
                        )

                        HistoryState.Loading -> HistoryState.Success(
                            localRoutes = it,
                            remoteRoutes = emptyList()
                        )

                        is HistoryState.Success -> copy(localRoutes = it)
                    }
                }
            }

            repository.getRoutes(pageIndex, PAGE_SIZE).getOrNull()
                ?.let {
                    if (it.message != null) {
                        updateState { HistoryState.Error(Text.Constant(it.message)) }
                    } else {
                        updateState {
                            when (this) {
                                is HistoryState.Error -> HistoryState.Success(
                                    remoteRoutes = it.routes.map { it.entity() },
                                    localRoutes = emptyList()
                                )

                                HistoryState.Loading -> HistoryState.Success(
                                    remoteRoutes = it.routes.map { it.entity() },
                                    localRoutes = emptyList()
                                )

                                is HistoryState.Success -> {
                                    pageIndex++
                                    copy(remoteRoutes = it.routes.map { it.entity() })
                                }
                            }
                        }
                    }
                }
                ?: run {
                    val needShowError =
                        (state as? HistoryState.Success)?.localRoutes?.isEmpty() == true
                    if (needShowError) {
                        updateState { HistoryState.Error(null) }
                    }
                }
        }.apply {
            invokeOnCompletion { cause ->
                if (cause is CancellationException) {
                    updateState { HistoryState.Error(null) }
                }
            }
        }
    }

    fun downloadRoute(previewEntity: PreviewEntity) {
        downloadJob?.cancel()
        downloadJob = viewModelScope.launch {
            val route = withTimeoutOrNull(TIMEOUT) {
                val badRoute = repository.getRoute(previewEntity.id).getOrNull()?.route
                badRoute?.copy(
                    path = badRoute.path.map { it.copy(it.lon, it.lat) }
                ) ?: badRoute
            }
            if (route != null) {
                repository.saveRoute(route)
                loadRoutes()
            } else {
                sideEffect {
                    DownloadError(
                        Text.Constant("Ошибка загрузки ${previewEntity.name}")
                    )
                }
            }
        }
    }

    fun removeRoute(previewEntity: PreviewEntity) {
        removeJob?.cancel()
        removeJob = viewModelScope.launch {
            val res = withTimeoutOrNull(TIMEOUT) {
                repository.removeRoute(previewEntity.name)
            }
            if (res == null) {
                sideEffect {
                    DownloadError(
                        Text.Constant("Ошибка удаления ${previewEntity.name}")
                    )
                }
            } else {
                loadRoutes()
            }
        }
    }

    fun navigateBack() = navigationProvider.navigateBack()

    fun navigateToPreview(route: PreviewEntity) {
        navigationJob?.cancel()
        navigationJob = viewModelScope.launch {
            val target = repository.getSavedRoute(route)
                ?: repository.getRoute(route.id).getOrNull()?.route?.entity()
            if (target != null) {
                navigationProvider.navigateToPreview(target)
            } else {
                sideEffect {
                    DownloadError(Text.Constant("Не удалось открыть маршрут"))
                }
            }
        }
    }

    @AssistedFactory
    interface Factory {
        fun create(): HistoryViewModel
    }

    private companion object {
        const val PAGE_SIZE = 15
        const val TIMEOUT = 5000L
    }
}

data class DownloadError(val title: Text) : SideEffect
