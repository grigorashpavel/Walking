package ru.pasha.feature.map.internal.presentation

import ru.pasha.common.pattern.BaseMapper
import ru.pasha.feature.map.internal.point

internal class MapMapper : BaseMapper<MapState, MapViewState>() {
    private var previousState: MapState? = null

    override fun toViewState(state: MapState): MapViewState {
        return MapViewState(
            isCenteredMarkerVisible = state.centerMarkerVisibility,
            zoom = state.zoom,
            location = state.center.point(),
            markersToDraw = state.createdMarkers,
            route = state.route?.path?.map { it.point() },
            markersToRemove = previousState
                ?.createdMarkers
                ?.minus(state.createdMarkers.toSet())
                ?: emptyList(),
        ).also { previousState = state }
    }
}
