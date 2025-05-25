package ru.pasha.feature.map.internal.presentation

import androidx.lifecycle.viewModelScope
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import ru.pasha.common.pattern.BaseViewModel
import ru.pasha.feature.map.internal.MapControllerProvider

internal class MapViewModel @AssistedInject constructor(
    mapControllerProvider: MapControllerProvider,
) : BaseViewModel<MapState, MapViewState>(
    mapper = MapMapper(),
    initialState = mapControllerProvider.createDefaultState()
) {
    init {
        mapControllerProvider.controllerFlow
            .onEach { updateState { it } }
            .launchIn(viewModelScope)
    }

    @AssistedFactory
    interface Factory {
        fun create(): MapViewModel
    }
}
