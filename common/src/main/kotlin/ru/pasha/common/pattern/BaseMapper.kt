package ru.pasha.common.pattern

abstract class BaseMapper<State, ViewState> {
    abstract fun toViewState(state: State): ViewState
}
