package ru.pasha.feature.home.internal.presentation

import androidx.lifecycle.viewModelScope
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import ru.pasha.common.Text
import ru.pasha.common.map.Marker
import ru.pasha.common.pattern.BaseViewModel
import ru.pasha.common.pattern.SideEffect
import ru.pasha.feature.home.api.WalkingMapProvider
import ru.pasha.feature.home.internal.view.CategoriesWidgetView

internal class HomeViewModel @AssistedInject constructor(
    private val walkingMapProvider: WalkingMapProvider,
) : BaseViewModel<HomeState, HomeViewState>(
    mapper = HomeMapper(walkingMapProvider.mapController::isReachedMaxMarkers),
    initialState = HomeState(
        category = Category.Nature,
        interactionModeEnabled = false,
        markers = emptyList(),
        isLoading = false
    ),
) {
    private var isFirstPoint = true

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
        viewModelScope.launch {
            updateState { copy(isLoading = true) }
            val error = walkingMapProvider.mapController.buildRoute(null)
            if (error != null) {
                sideEffect { Error(error) }
            }
            updateState { copy(isLoading = false) }
        }
    }

    fun accessLocation() {
        walkingMapProvider.mapController.accessLocation()
    }

    fun toggleInteractionMode(target: Boolean) {
        updateState { copy(interactionModeEnabled = target) }
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
        fun create(): HomeViewModel
    }
}

data class Error(val title: Text) : SideEffect
data class FirstPoi(val title: Text, val subtitle: Text) : SideEffect
