package ru.pasha.feature.map.internal.presentation

import ru.pasha.common.pattern.BaseMapper

internal class MapMapper : BaseMapper<MapState, MapViewState>() {
    override fun toViewState(state: MapState): MapViewState {
        return MapViewState(state.t)
    }
}
