package ru.pasha.common.pattern

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update

abstract class BaseViewModel<State, ViewState>(
    private val mapper: BaseMapper<State, ViewState>,
    initialState: State,
) : ViewModel() {
    private val _state by lazy { MutableStateFlow<State>(initialState) }
    protected var previousState: State? = null
        private set
    protected val previousViewState: ViewState? get() = previousState?.let(mapper::toViewState)

    protected val state get() = _state.value
    protected val viewState get() = mapper.toViewState(state)

    private val _sideEffects by lazy {
        MutableSharedFlow<SideEffect>(
            extraBufferCapacity = 6,
            onBufferOverflow = BufferOverflow.DROP_OLDEST
        )
    }

    val viewStates: Flow<ViewState> = _state.map(mapper::toViewState)
    val sideEffects: Flow<SideEffect> = _sideEffects

    protected fun updateState(transformer: State.() -> State) {
        previousState = _state.value
        _state.update(transformer)
    }
    protected fun sideEffect(effect: () -> SideEffect) = _sideEffects.tryEmit(effect())
}

interface SideEffect
