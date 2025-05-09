package ru.pasha.feature_start_preview.internal.presentation

internal sealed interface AuthState {
    data object Loading: PreviewState
    data object Error: PreviewState
}