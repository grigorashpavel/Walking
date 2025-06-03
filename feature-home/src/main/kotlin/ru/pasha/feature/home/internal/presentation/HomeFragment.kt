package ru.pasha.feature.home.internal.presentation

import android.animation.ObjectAnimator
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.MarginLayoutParams
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.animation.doOnEnd
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.isVisible
import androidx.core.view.updateLayoutParams
import androidx.core.view.updateMargins
import androidx.core.view.updatePadding
import com.github.terrakok.cicerone.Cicerone
import com.github.terrakok.cicerone.Router
import com.github.terrakok.cicerone.androidx.AppNavigator
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.snackbar.Snackbar
import ru.pasha.common.di.WalkingMapProvider
import ru.pasha.common.format
import ru.pasha.common.pattern.BaseFragment
import ru.pasha.common.pattern.SideEffect
import ru.pasha.common.pattern.extractScreenParams
import ru.pasha.feature.home.R
import ru.pasha.feature.home.databinding.HomeFragmentBinding
import ru.pasha.feature.home.internal.view.HomeBottomSheetCallback
import ru.pasha.feature.home.internal.view.HomeBottomSheetView
import ru.pasha.feature.home.internal.view.TopPanelBehaviour
import javax.inject.Inject

@Suppress("TooManyFunctions")
internal class HomeFragment @Inject constructor(
    private val viewModelFactory: HomeViewModel.Factory,
    private val walkingMapProvider: WalkingMapProvider,
) : BaseFragment<HomeViewState, HomeViewModel, HomeFragmentBinding>(
    viewModelClass = HomeViewModel::class.java
) {
    private val cicerone = Cicerone.create(Router())

    private var previousState: HomeViewState? = null

    private var bottomSheetBehavior: BottomSheetBehavior<HomeBottomSheetView>? = null
    private var bottomSheetCallback: BottomSheetBehavior.BottomSheetCallback? = null

    private var topPanelBehavior: TopPanelBehaviour? = null

    private val navigation by lazy {
        AppNavigator(
            activity = requireActivity(),
            containerId = binding.homeWalkingMapContainer.id,
            fragmentManager = childFragmentManager,
            fragmentFactory = walkingMapProvider.fragmentFactory
        )
    }

    override fun createViewModel(): HomeViewModel = viewModelFactory.create(extractScreenParams())

    override fun onApplyInsets(insets: WindowInsetsCompat): WindowInsetsCompat {
        val barsInsets = insets.getInsets(WindowInsetsCompat.Type.systemBars())
        binding.homeCategoriesWidget.updateLayoutParams<MarginLayoutParams> {
            updateMargins(top = barsInsets.top / 2)
        }

        binding.homeShimmerLayout.root.updatePadding(
            top = barsInsets.top / 2
        )
        return insets
    }

    override fun getViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): HomeFragmentBinding {
        return HomeFragmentBinding.inflate(inflater, container, false).apply {
            bottomSheetBehavior = BottomSheetBehavior.from(homeBottomSheet).apply {
                state = BottomSheetBehavior.STATE_HALF_EXPANDED
                halfExpandedRatio = SHEET_HALF_RATIO

                isFitToContents = false
                isHideable = false
                isDraggable = false
            }.apply {
                bottomSheetCallback = HomeBottomSheetCallback(this).also {
                    bottomSheetCallback = it
                }
            }

            val params = homeTopPanel.layoutParams as CoordinatorLayout.LayoutParams
            topPanelBehavior = params.behavior as TopPanelBehaviour
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        if (savedInstanceState == null) {
            cicerone.router.replaceScreen(screen = walkingMapProvider.mapFragmentScreen())
        }
        super.onViewCreated(view, savedInstanceState)

        viewModel.onViewCreated()

        renderShimmer()
        setupCategoriesListener()
        setupButtonsListeners()
        binding.homeBottomSheet.setListeners(
            onRemoveMarkers = viewModel::removeMarkers,
            onRemoveMarker = viewModel::removeMarker,
            navigateToHistory = viewModel::navigateToHistory,
            navigateToSettings = viewModel::navigateToSettings,
        )
    }

    override fun render(viewState: HomeViewState) {
        binding.homeCategoriesWidget.setState(viewState.categoryState)
        binding.homeMarkerButton.isVisible = viewState.markerButtonVisible

        binding.homeBottomSheet.render(viewState.sheetContentState)
        renderPoisButton(viewState)
        renderPreviewMode(viewState)
        renderButtons(viewState)

        previousState = viewState
    }

    override fun onResume() {
        super.onResume()
        cicerone.getNavigatorHolder().setNavigator(navigation)
    }

    override fun onPause() {
        super.onPause()
        cicerone.getNavigatorHolder().removeNavigator()
    }

    override fun onDestroyView() {
        bottomSheetCallback?.let { bottomSheetBehavior?.removeBottomSheetCallback(it) }
        bottomSheetBehavior = null
        bottomSheetCallback = null

        topPanelBehavior = null
        super.onDestroyView()
    }

    override fun onDetach() {
        super.onDetach()
        cicerone.router.exit()
    }

    private fun setupButtonsListeners() {
        binding.homeStartButton.setOnClickListener {
            startMapInteraction()
        }
        binding.homeBackButton.setOnClickListener {
            endMapInteraction()
        }
        binding.homeMarkerButton.setOnClickListener {
            viewModel.tryCreateMarker()
        }
        binding.homeBuildRouteButton.setOnClickListener {
            viewModel.buildRoute()
        }
        binding.homeLocationButton.setOnClickListener {
            viewModel.switchLocation(enabled = true)
        }
        binding.homeWalkingButton.setOnClickListener {
            viewModel.toggleWalkingMode()
        }
    }

    private fun startMapInteraction() {
        removeCategoriesListener()

        topPanelBehavior?.togglePanelState(binding.homeTopPanel, show = false)
        bottomSheetBehavior?.state = BottomSheetBehavior.STATE_COLLAPSED
        bottomSheetBehavior?.isDraggable = true

        viewModel.toggleMapState(willInteraction = true)
        viewModel.toggleInteractionMode(true)
    }

    private fun endMapInteraction() {
        setupCategoriesListener()

        topPanelBehavior?.togglePanelState(binding.homeTopPanel, show = true)
        bottomSheetBehavior?.state = BottomSheetBehavior.STATE_HALF_EXPANDED
        bottomSheetBehavior?.isDraggable = false

        viewModel.toggleMapState(willInteraction = false)
        viewModel.setWalkingMode(false)
        viewModel.toggleInteractionMode(false)
        viewModel.switchLocation(enabled = false)
    }

    private fun renderPoisButton(viewState: HomeViewState) {
        binding.homeMarkerButton.isEnabled = viewState.addMarkerEnabled
        binding.homeMarkerButton.alpha = if (viewState.addMarkerEnabled) {
            ACTIVE_ALPHA
        } else {
            INACTIVE_ALPHA
        }

        binding.homeWalkingButton.isVisible = viewState.hasRoute
        binding.homeWalkingButton.setImageResource(
            if (viewState.walkingModeEnabled) R.drawable.ic_stop_24 else R.drawable.ic_start_24
        )
    }

    private fun renderShimmer() {
        binding.homeShimmerLayout.root.safeAnimate {
            ObjectAnimator
                .ofFloat(binding.homeShimmerLayout.root, View.ALPHA, 1f, 0f).apply {
                    duration = SHIMMER_HIDING_DURATION
                    doOnEnd {
                        binding.homeShimmerLayout.root.stopShimmer()
                        binding.homeShimmerLayout.root.isVisible = false
                        binding.root.removeView(binding.homeShimmerLayout.root)
                    }
                }
        }
    }

    private fun setupCategoriesListener() {
        binding.homeCategoriesWidget.setStateListener(viewModel::categoriesStateSelected)
    }

    private fun removeCategoriesListener() {
        binding.homeCategoriesWidget.setStateListener {}
    }

    private fun renderButtons(viewState: HomeViewState) {
        val needShow = !viewState.walkingModeEnabled && !viewState.isPreviewMode
        binding.homeMarkerButton.isVisible = needShow && !viewState.hasRoute
        binding.homeBuildRouteButton.isVisible = needShow && !viewState.hasRoute
        binding.homeLocationButton.isVisible = viewState.isLocationButtonVisible
    }

    private fun renderPreviewMode(viewState: HomeViewState) {
        if (previousState?.isPreviewMode == viewState.isPreviewMode) return
        viewModel.toggleMapState(willInteraction = viewState.isPreviewMode)
        safePost {
            topPanelBehavior?.togglePanelState(binding.homeTopPanel, !viewState.isPreviewMode)
            if (viewState.isPreviewMode) {
                bottomSheetBehavior?.state = BottomSheetBehavior.STATE_COLLAPSED
                bottomSheetBehavior?.isDraggable = true
            }
        }
        binding.homeBuildRouteButton.isVisible = !viewState.isPreviewMode
        binding.homeMarkerButton.isVisible = !viewState.isPreviewMode
    }

    override fun consumeSideEffect(effect: SideEffect) = when (effect) {
        is FirstPoi -> {
            val message = effect.title
                .format(requireContext())
                .toString() + "\n" + effect.subtitle.format(requireContext()).toString()
            showSnackbar(message)
        }

        is Error -> showSnackbar(effect.title.format(requireContext()).toString())

        else -> {}
    }

    private fun showSnackbar(message: String) {
        Snackbar.make(
            binding.root,
            message,
            Snackbar.LENGTH_LONG,
        ).apply {
            setBackgroundTint(
                resources.getColor(
                    ru.pasha.common.R.color.walking_app_light_500,
                    requireActivity().theme
                )
            )
            anchorView = binding.homeButtonsContainer
        }.show()
    }

    private companion object {
        const val SHEET_HALF_RATIO = .5f

        const val ACTIVE_ALPHA = 1f
        const val INACTIVE_ALPHA = 0.5f

        const val SHIMMER_HIDING_DURATION = 2000L
    }
}
