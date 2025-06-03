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
import ru.pasha.feature.home.api.LocationTrackerSettingProvider
import ru.pasha.feature.home.internal.data.HomeRepository
import ru.pasha.feature.home.internal.view.CategoriesWidgetView

internal class HomeViewModel @AssistedInject constructor(
    private val walkingMapProvider: WalkingMapProvider,
    private val navigationProvider: HomeNavigationProvider,
    @Assisted
    private val homeArguments: HomeArguments,
    private val locationTrackerSettingProvider: LocationTrackerSettingProvider,
    private val homeRepository: HomeRepository,
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
        locationTrackingEnabled = locationTrackerSettingProvider.isEnabled,
    ),
) {
    private var isFirstPoint = true
    private var needShowFeedback = false

    private var buildJob: Job? = null

    init {
        walkingMapProvider.mapController.markers
            .onEach { markers ->
                if (isFirstPoint && markers.isNotEmpty()) {
                    isFirstPoint = false
                    sideEffect {
                        FirstPoi(
                            title = Text.Resource(ru.pasha.common.R.string.walking_app_routes_poi_redacted_title),
                            subtitle = Text.Resource(ru.pasha.common.R.string.walking_app_routes_poi_redacted_subtitle),
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

    fun onViewCreated() {
        updateState {
            copy(locationTrackingEnabled = locationTrackerSettingProvider.isEnabled)
        }
    }

    fun navigateToHistory() {
        navigationProvider.navigateToHistory()
    }

    fun navigateToSettings() {
        navigationProvider.navigateToSettings()
    }

    fun toggleWalkingMode(target: Boolean? = null) {
        val canTrackLocation = state.locationTrackingEnabled

        updateState {
            val toEnabled = target ?: !walkingModeEnabled

            if (toEnabled) needShowFeedback = true

            if (canTrackLocation) {
                switchLocation(toEnabled)
            }
            walkingMapProvider.mapController.setWalkingMode(toEnabled)
            walkingMapProvider.mapController.setCenterMarkerVisibility(!toEnabled)
            copy(walkingModeEnabled = toEnabled)
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

    fun switchLocation(enabled: Boolean) {
        walkingMapProvider.mapController.switchLocationListen(enabled = enabled)
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

    fun tryShowFeedback() {
        if (state.route != null && needShowFeedback) {
            needShowFeedback = false
            sideEffect { Feedback }
        }
    }

    fun reportRouteFeedback(message: String, rating: Int) {
        viewModelScope.launch {
            homeRepository.reportRouteFeedback(message, rating)
        }
    }

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
data object Feedback : SideEffect
