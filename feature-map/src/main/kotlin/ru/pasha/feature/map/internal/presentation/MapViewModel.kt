package ru.pasha.feature.map.internal.presentation

import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import ru.pasha.common.pattern.BaseViewModel

internal class MapViewModel @AssistedInject constructor() : BaseViewModel<MapState, MapViewState>(
    mapper = MapMapper(),
    initialState = MapState(t = "")
) {

    @AssistedFactory
    interface Factory {
        fun create(): MapViewModel
    }
}
