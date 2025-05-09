package ru.pasha.feature_start_preview.internal.presentation

import ru.pasha.common.BaseMapper
import ru.pasha.common.BaseViewModel
import ru.pasha.common.ImageModel

internal class AuthViewModel(

) : BaseViewModel<PreviewState, PreviewViewState>(
    mapper = object : BaseMapper<PreviewState, PreviewViewState>() {
        override fun toViewState(state: PreviewState): PreviewViewState {
            return PreviewViewState(ImageModel(""))
        }

    },
    initialState = PreviewState.Loading
) {

}