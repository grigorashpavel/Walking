package ru.pasha.feature.map.internal.presentation

import androidx.lifecycle.viewModelScope
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import ru.pasha.common.pattern.BaseViewModel
import ru.pasha.common.pattern.SideEffect
import ru.pasha.feature.map.internal.MapControllerProvider

internal class MapViewModel @AssistedInject constructor(
    mapControllerProvider: MapControllerProvider,
    private val permissionManager: MapPermissionManager,
) : BaseViewModel<MapState, MapViewState>(
    mapper = MapMapper(),
    initialState = mapControllerProvider.createDefaultState()
) {
    init {
        mapControllerProvider.controllerFlow
            .map {
                it.copy(createdMarkers = if (it.walkingModeEnabled) emptyList() else it.createdMarkers)
            }
            .onEach { updateState { it } }
            .launchIn(viewModelScope)

        mapControllerProvider.locationCallback = { enable ->
            if (enable) {
                checkLocationPermissions()
            } else {
                stopLocationTracking()
            }
        }
        mapControllerProvider.stepsCallback = { enabled ->
            if (enabled) {
                checkStepsPermission()
            } else {
                stopListenSteps()
            }
        }
    }

    private fun checkLocationPermissions() {
        if (!permissionManager.hasLocationPermission()) {
            sideEffect {
                MapSideEffect.RequestLocationPermission
            }
        } else {
            startLocationTracking()
        }
    }

    private fun checkStepsPermission() {
        if (!permissionManager.hasStepsPermissions()) {
            sideEffect {
                MapSideEffect.RequestStepsPermission
            }
        } else {
            startListenSteps()
        }
    }

    private fun startListenSteps() {
        sideEffect { MapSideEffect.StartListenSteps }
    }

    private fun stopListenSteps() {
        sideEffect { MapSideEffect.StopListenSteps }
    }

    private fun startLocationTracking() {
        sideEffect { MapSideEffect.StartListenLocation }
    }

    private fun stopLocationTracking() {
        sideEffect { MapSideEffect.StopListenLocation }
    }

    fun handleLocationPermissionResult(granted: Boolean) {
        if (granted) {
            startLocationTracking()
        }
    }

    fun handleStepsPermissionResult(granted: Boolean) {
        if (granted) {
            startListenSteps()
        }
    }

    @AssistedFactory
    interface Factory {
        fun create(): MapViewModel
    }
}

sealed class MapSideEffect : SideEffect {
    data object RequestLocationPermission : MapSideEffect()
    data object StartListenLocation : MapSideEffect()
    data object StopListenLocation : MapSideEffect()
    data object RequestStepsPermission : MapSideEffect()
    data object StartListenSteps : MapSideEffect()
    data object StopListenSteps : MapSideEffect()
}
