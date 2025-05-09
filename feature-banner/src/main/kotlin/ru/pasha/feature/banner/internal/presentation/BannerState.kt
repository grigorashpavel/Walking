package ru.pasha.feature.banner.internal.presentation

import ru.pasha.common.Text
import ru.pasha.feature.banner.internal.domain.BannerEntity

internal sealed interface BannerState {
    data object Loading : BannerState
    data class Success(val banners: List<BannerEntity>, val buttonText: Text) : BannerState
    data class Error(val message: String) : BannerState
}
