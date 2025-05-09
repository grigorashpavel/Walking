package ru.pasha.feature.banner.internal.presentation

import ru.pasha.common.Text
import ru.pasha.feature.banner.internal.domain.BannerEntity

sealed interface BannerViewState {
    data object Loading : BannerViewState

    data class Error(val title: Text, val subtitle: Text) : BannerViewState

    data class Success(
        val banners: List<BannerEntity>,
        val showProgress: Boolean,
        val buttonText: Text
    ) : BannerViewState
}
