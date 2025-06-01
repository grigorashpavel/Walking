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
                it.copy(
                    createdMarkers = if (it.walkingModeEnabled) emptyList() else it.createdMarkers
                )
            }
            .onEach { updateState { it } }
            .launchIn(viewModelScope)

        mapControllerProvider.locationCallback = ::checkLocationPermissions
    }

    fun checkLocationPermissions() {
        if (!permissionManager.hasLocationPermission()) {
            sideEffect {
                MapSideEffect.RequestLocationPermission
            }
        } else {
            startLocationTracking()
        }
    }

    private fun startLocationTracking() {
        sideEffect { MapSideEffect.StartListenLocation }
    }

    fun handlePermissionResult(granted: Boolean) {
        if (granted) {
            startLocationTracking()
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
}
