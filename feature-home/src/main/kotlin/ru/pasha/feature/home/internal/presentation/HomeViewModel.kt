@file:Suppress("TooManyFunctions")

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
import ru.pasha.feature.home.api.SettingsProvider
import ru.pasha.feature.home.internal.data.DefaultWeights
import ru.pasha.feature.home.internal.data.HomeRepository
import ru.pasha.feature.home.internal.data.NatureWeights
import ru.pasha.feature.home.internal.data.PedestrianWeights
import ru.pasha.feature.home.internal.data.ResidentialWeights
import ru.pasha.feature.home.internal.view.CategoriesWidgetView

internal class HomeViewModel @AssistedInject constructor(
    private val walkingMapProvider: WalkingMapProvider,
    private val navigationProvider: HomeNavigationProvider,
    @Assisted
    private val homeArguments: HomeArguments,
    private val settingsProvider: SettingsProvider,
    private val homeRepository: HomeRepository,
    private val timerController: TimerController,
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
        locationTrackingEnabled = settingsProvider.locationTrackingEnabled,
        stepsTrackingEnabled = settingsProvider.stepsTrackingEnabled,
        steps = null,
        walkingTime = null,
        walkingStarted = false,
    ),
) {
    private var isFirstPoint = true
    private var walkingWasStarted = false
    private var canShowRouteNameDialog = true

    private var buildJob: Job? = null

    init {
        walkingMapProvider.mapController.route
            .onEach { route ->
                updateState {
                    copy(route = route)
                }
            }
            .launchIn(viewModelScope)

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

        timerController.time.onEach {
            updateState { copy(walkingTime = it) }
        }.launchIn(viewModelScope)

        if (settingsProvider.stepsTrackingEnabled) {
            walkingMapProvider.mapController.stepsFlow
                .onEach {
                    updateState { copy(steps = it) }
                }
                .launchIn(viewModelScope)
        }

        homeArguments.route?.let {
            walkingMapProvider.mapController.setRoute(it)
            walkingMapProvider.mapController.setPreviewMode(true)
        }
    }

    fun onViewCreated() {
        updateState {
            copy(
                locationTrackingEnabled = settingsProvider.locationTrackingEnabled,
                stepsTrackingEnabled = settingsProvider.stepsTrackingEnabled,
            )
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
        val canTrackSteps = state.stepsTrackingEnabled

        updateState {
            val toEnabled = target ?: !walkingModeEnabled

            if (!walkingWasStarted && toEnabled) {
                sideEffect { ResetStepsCount }
            }
            if (toEnabled) {
                updateState { copy(walkingStarted = true) }
                walkingWasStarted = true
            }

            if (toEnabled) {
                timerController.start()
                sideEffect { StartStepsCount }
            } else {
                sideEffect { StopStepsCount }
                timerController.pause()
            }

            if (canTrackLocation) {
                switchLocation(toEnabled)
            }
            if (canTrackSteps) {
                switchStepsOption(toEnabled)
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

    fun resetTimer() {
        timerController.reset()
    }

    private fun buildRoute(routeName: String) {
        buildJob?.cancel()
        buildJob = viewModelScope.launch {
            val weights = when (state.category) {
                Category.Nature -> NatureWeights
                Category.Residential -> ResidentialWeights
                Category.Roads -> PedestrianWeights
                Category.None -> DefaultWeights
            }

            updateState { copy(isLoading = true) }
            walkingMapProvider.mapController.setCenterMarkerVisibility(show = false)
            val error = walkingMapProvider.mapController.buildRoute(name = routeName, weights)
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

    private fun switchStepsOption(enabled: Boolean) {
        walkingMapProvider.mapController.switchStepsListen(enabled)
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
        if (state.route != null && walkingWasStarted) {
            updateState { copy(walkingStarted = false) }
            walkingWasStarted = false
            sideEffect { Feedback }
        }
    }

    fun reportRouteFeedback(message: String, rating: Int) {
        viewModelScope.launch {
            homeRepository.reportRouteFeedback(message, rating)
        }
    }

    fun showRouteNameDialog() {
        if (canShowRouteNameDialog) sideEffect { RouteName }
    }

    fun onRouteNameDialogOpened() {
        canShowRouteNameDialog = false
    }

    fun onRouteNameDialogClosed(routeName: String?) {
        canShowRouteNameDialog = true
        if (routeName != null) {
            if (routeName.length !in ROUTE_NAME_LEN) {
                sideEffect {
                    RouteBadName(Text.Resource(ru.pasha.common.R.string.walking_app_route_name_error))
                }
            } else {
                buildRoute(routeName)
            }
        } else {
            sideEffect { RouteBadName(Text.Resource(ru.pasha.common.R.string.walking_app_route_not_named)) }
        }
    }

    private fun CategoriesWidgetView.Category.toSimpleCategory(): Category {
        return when (this) {
            CategoriesWidgetView.Category.NATURE -> Category.Nature
            CategoriesWidgetView.Category.RESIDENTIAL -> Category.Residential
            CategoriesWidgetView.Category.ROADS -> Category.Roads
        }
    }

    override fun onCleared() {
        timerController.reset()
        super.onCleared()
    }

    @AssistedFactory
    interface Factory {
        fun create(arguments: HomeArguments): HomeViewModel
    }

    private companion object {
        val ROUTE_NAME_LEN = 5..45
    }
}

data class Error(val title: Text) : SideEffect
data class FirstPoi(val title: Text, val subtitle: Text) : SideEffect
data object Feedback : SideEffect
data object RouteName : SideEffect
data object StartStepsCount : SideEffect
data object ResetStepsCount : SideEffect
data object StopStepsCount : SideEffect
data class RouteBadName(val message: Text) : SideEffect
