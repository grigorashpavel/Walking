package ru.pasha.feature.banner.internal.presentation

import androidx.lifecycle.viewModelScope
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import ru.pasha.common.ScaleType
import ru.pasha.common.Text
import ru.pasha.common.models.ImageModel
import ru.pasha.common.pattern.BaseViewModel
import ru.pasha.common.pattern.SideEffect
import ru.pasha.feature.banner.R
import ru.pasha.feature.banner.api.BannerNavigationProvider
import ru.pasha.feature.banner.api.ConnectionProvider
import ru.pasha.feature.banner.internal.domain.BannerEntity

internal class BannerViewModel @AssistedInject constructor(
    private val connectionProvider: ConnectionProvider,
    private val navigationProvider: BannerNavigationProvider,
    @Assisted
    private val authAction: suspend () -> Boolean
) : BaseViewModel<BannerState, BannerViewState>(
    mapper = BannerMapper(),
    initialState = BannerState.Loading
) {
    private var navJob: Job? = null

    init { loadBanners() }

    private fun loadBanners() {
        viewModelScope.launch {
            val banners = withContext(Dispatchers.IO) {
                delay(500)

                listOf(
                    BannerEntity(
                        title = Text.Resource(ru.pasha.common.R.string.walking_app_intro_slide1_title),
                        subtitle = Text.Resource(ru.pasha.common.R.string.walking_app_intro_slide1_subtitle),
                        image = ImageModel.Res(
                            id = R.drawable.ic_doodle,
                            scaleType = ScaleType.FIT_CENTER
                        )
                    ),
                    BannerEntity(
                        title = Text.Resource(ru.pasha.common.R.string.walking_app_intro_slide2_title),
                        subtitle = Text.Resource(ru.pasha.common.R.string.walking_app_intro_slide2_subtitle),
                        image = ImageModel.Res(
                            id = R.drawable.ic_banner2,
                            scaleType = ScaleType.FIT_XY
                        )
                    ),
                    BannerEntity(
                        title = Text.Resource(ru.pasha.common.R.string.walking_app_intro_slide3_title),
                        subtitle = Text.Resource(ru.pasha.common.R.string.walking_app_intro_slide3_subtitle),
                        image = ImageModel.Res(
                            id = R.drawable.ic_water_banner,
                            scaleType = ScaleType.FIT_XY
                        )
                    ),
                    BannerEntity(
                        title = Text.Resource(ru.pasha.common.R.string.walking_app_intro_slide4_title),
                        subtitle = Text.Resource(ru.pasha.common.R.string.walking_app_intro_slide4_subtitle),
                        image = null
                    ),
                )
            }
            updateState {
                BannerState.Success(
                    banners,
                    buttonText = Text.Resource(ru.pasha.common.R.string.walking_app_intro_button_text)
                )
            }
        }
    }

    fun tryNavigate() {
        navJob?.cancel()
        if (!connectionProvider.hasConnection) {
            return navigateToHome()
        }

        navJob = viewModelScope.launch {
            val authed = authAction()
            if (authed) navigateToHome()
        }
    }

    private fun navigateToHome() = navigationProvider.navigateToHomeAction()

    fun retryLoading() {
        updateState { BannerState.Loading }
        loadBanners()
    }

    @AssistedFactory
    interface Factory {
        fun create(authAction: suspend () -> Boolean): BannerViewModel
    }
}

sealed class BannerSideEffect : SideEffect {
    data class ShowError(val message: Text) : BannerSideEffect()
}
