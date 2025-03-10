package ru.pasha.common

abstract class BaseMapper<State, ViewState> {
    abstract fun toViewState(state: State): ViewState
}
