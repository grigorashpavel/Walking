package ru.pasha.feature.banner.internal.presentation

import ru.pasha.common.Text
import ru.pasha.common.pattern.BaseMapper

internal class BannerMapper : BaseMapper<BannerState, BannerViewState>() {
    override fun toViewState(state: BannerState): BannerViewState {
        return when (state) {
            is BannerState.Loading -> BannerViewState.Loading
            is BannerState.Success -> BannerViewState.Success(
                banners = state.banners,
                showProgress = state.banners.size > 1,
                buttonText = state.buttonText,
            )
            is BannerState.Error -> BannerViewState.Error(
                title = Text.Empty,
                subtitle = Text.Empty,
            )
        }
    }
}
