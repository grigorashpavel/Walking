package ru.pasha.feature.map.internal

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import ru.pasha.common.map.GeoPoint
import ru.pasha.common.map.MapController
import ru.pasha.common.map.Marker
import ru.pasha.feature.map.api.MapSettingsProvider
import ru.pasha.feature.map.internal.di.MapScope
import ru.pasha.feature.map.internal.presentation.MapState
import javax.inject.Inject

@MapScope
internal class MapControllerProvider @Inject constructor(
    private val settingsProvider: MapSettingsProvider
) : MapController {
    val controllerFlow = MutableStateFlow(createDefaultState())

    override val currentLocation: GeoPoint get() = controllerFlow.value.center

    override val isMarkerVisible: Boolean get() = controllerFlow.value.centerMarkerVisibility
    override val zoom get() = controllerFlow.value.zoom

    override fun setCurrentLocation(point: GeoPoint) {
        update { copy(center = point, stateUpdatingFlag = false) }
    }

    override fun setZoom(zoom: Double) {
        update { copy(zoom = zoom, stateUpdatingFlag = false) }
    }

    override fun setCenterMarkerVisibility(show: Boolean) {
        update { copy(centerMarkerVisibility = show, stateUpdatingFlag = false) }
    }

    override fun toggleCreateMarkerFeature(isEnabled: Boolean) {
        update { copy(createMarkerOptionEnabled = isEnabled, stateUpdatingFlag = false) }
    }

    override fun restoreMap() {
        update { createDefaultState() }
    }

    override val markers: Flow<List<Marker>> get() = controllerFlow.map { it.createdMarkers }

    override fun createMarker(): Boolean {
        val currentSize = controllerFlow.value.createdMarkers.size
        if (currentSize >= settingsProvider.maxPois) return false
        val targetColor = settingsProvider.markersColor.getOrNull(currentSize) ?: return false

        update {
            copy(createdMarkers = createdMarkers.toMutableList()
                .apply {
                    add(Marker(id = currentLocation.hashCode(), currentLocation, targetColor))
                }
            )
        }
        return true
    }

    private fun createDefaultState(): MapState {
        return MapState(
            settingsProvider.startZoom,
            settingsProvider.startLocation,
            settingsProvider.isCenterMarkerVisible,
            createMarkerOptionEnabled = false,
            createdMarkers = listOf(),
            stateUpdatingFlag = true,
        )
    }

    private fun update(transformer: MapState.() -> MapState) {
        controllerFlow.update(transformer)
    }
}
