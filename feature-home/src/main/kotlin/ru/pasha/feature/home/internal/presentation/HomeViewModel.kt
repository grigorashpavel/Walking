package ru.pasha.feature.home.internal.presentation

import androidx.lifecycle.viewModelScope
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import ru.pasha.common.Text
import ru.pasha.common.di.WalkingMapProvider
import ru.pasha.common.map.Marker
import ru.pasha.common.pattern.BaseViewModel
import ru.pasha.common.pattern.SideEffect
import ru.pasha.feature.home.api.HomeArguments
import ru.pasha.feature.home.api.HomeNavigationProvider
import ru.pasha.feature.home.internal.view.CategoriesWidgetView

internal class HomeViewModel @AssistedInject constructor(
    private val walkingMapProvider: WalkingMapProvider,
    private val navigationProvider: HomeNavigationProvider,
    @Assisted
    private val homeArguments: HomeArguments,
) : BaseViewModel<HomeState, HomeViewState>(
    mapper = HomeMapper(walkingMapProvider.mapController::isReachedMaxMarkers),
    initialState = HomeState(
        category = Category.Nature,
        interactionModeEnabled = homeArguments.route != null,
        markers = emptyList(),
        isLoading = false,
        walkingModeEnabled = false,
        route = homeArguments.route,
        previewModeEnabled = homeArguments.route != null,
    ),
) {
    private var isFirstPoint = true

    private var buildJob: Job? = null

    init {
        walkingMapProvider.mapController.markers
            .onEach { markers ->
                if (isFirstPoint && markers.isNotEmpty()) {
                    isFirstPoint = false
                    sideEffect {
                        FirstPoi(
                            title = Text.Constant("Отредактировали вашу точку"),
                            subtitle = Text.Constant("Пока что строим только через перекрестки"),
                        )
                    }
                }
                updateState { copy(markers = markers) }
            }
            .launchIn(viewModelScope)

        homeArguments.route?.let {
            walkingMapProvider.mapController.setRoute(it)
            walkingMapProvider.mapController.setPreviewMode(true)
        }

        walkingMapProvider.mapController.route
            .onEach { route ->
                updateState { copy(route = route) }
            }
            .launchIn(viewModelScope)
    }

    fun navigateToHistory() {
        navigationProvider.navigateToHistory()
    }

    fun toggleWalkingMode() {
        updateState {
            if (!walkingModeEnabled) accessLocation()
            walkingMapProvider.mapController.setWalkingMode(!walkingModeEnabled)
            copy(walkingModeEnabled = !walkingModeEnabled)
        }
    }

    fun categoriesStateSelected(state: CategoriesWidgetView.State) {
        val category = when (state) {
            CategoriesWidgetView.State.Default -> Category.None
            is CategoriesWidgetView.State.User -> state.category?.toSimpleCategory()
        } ?: previousState?.category ?: Category.Nature

        if (category == this.state.category) return
        updateState { copy(category = category) }
    }

    fun tryCreateMarker() {
        viewModelScope.launch {
            walkingMapProvider.mapController.createMarker()
        }
    }

    fun buildRoute() {
        buildJob?.cancel()
        buildJob = viewModelScope.launch {
            updateState { copy(isLoading = true) }
            walkingMapProvider.mapController.setCenterMarkerVisibility(show = false)
            val error = walkingMapProvider.mapController.buildRoute(null)
            if (error != null) {
                walkingMapProvider.mapController.setCenterMarkerVisibility(show = true)
                sideEffect { Error(error) }
            }
            updateState { copy(isLoading = false) }
        }.apply {
            invokeOnCompletion {
                if (this.isCancelled) {
                    walkingMapProvider.mapController.setCenterMarkerVisibility(show = true)
                }
            }
        }
    }

    fun accessLocation() {
        walkingMapProvider.mapController.accessLocation()
    }

    fun toggleInteractionMode(target: Boolean) {
        if (target) {
            updateState { copy(interactionModeEnabled = true) }
        } else {
            walkingMapProvider.mapController.setPreviewMode(false)
            updateState { copy(interactionModeEnabled = false, previewModeEnabled = false) }
        }
    }

    fun toggleMapState(willInteraction: Boolean) {
        if (!willInteraction) {
            walkingMapProvider.mapController.restoreMap()
            return
        }

        walkingMapProvider.mapController.setCenterMarkerVisibility(true)
        walkingMapProvider.mapController.toggleCreateMarkerFeature(true)
    }

    fun removeMarkers() = walkingMapProvider.mapController.removeMarkers()

    fun removeMarker(marker: Marker) = walkingMapProvider.mapController.removeMarker(marker)

    private fun CategoriesWidgetView.Category.toSimpleCategory(): Category {
        return when (this) {
            CategoriesWidgetView.Category.NATURE -> Category.Nature
            CategoriesWidgetView.Category.RESIDENTIAL -> Category.Residential
            CategoriesWidgetView.Category.ROADS -> Category.Roads
        }
    }

    @AssistedFactory
    interface Factory {
        fun create(arguments: HomeArguments): HomeViewModel
    }
}

data class Error(val title: Text) : SideEffect
data class FirstPoi(val title: Text, val subtitle: Text) : SideEffect
