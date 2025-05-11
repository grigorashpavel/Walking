package ru.pasha.feature.home.internal.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.isVisible
import com.github.terrakok.cicerone.Cicerone
import com.github.terrakok.cicerone.Router
import com.github.terrakok.cicerone.androidx.AppNavigator
import com.google.android.material.bottomsheet.BottomSheetBehavior
import ru.pasha.common.pattern.BaseFragment
import ru.pasha.common.pattern.SideEffect
import ru.pasha.feature.home.api.WalkingMapProvider
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

    override fun createViewModel(): HomeViewModel = viewModelFactory.create()

    override fun onApplyInsets(insets: WindowInsetsCompat): WindowInsetsCompat {
        return insets
    }

    override fun getViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): HomeFragmentBinding {
        return HomeFragmentBinding.inflate(inflater, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        if (savedInstanceState == null) {
            cicerone.router.replaceScreen(screen = walkingMapProvider.mapFragmentScreen())
        }
        super.onViewCreated(view, savedInstanceState)

        setupTopPanelBehaviour()
        setupCategoriesListener()
        setupBottomSheetBehaviour()
        setupButtonsListeners()
    }

    override fun render(viewState: HomeViewState) {
        binding.homeCategoriesWidget.setState(viewState.categoryState)
        binding.homeMarkerButton.isVisible = viewState.markerButtonVisible
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

    private fun setupBottomSheetBehaviour() {
        bottomSheetBehavior = BottomSheetBehavior.from(binding.homeBottomSheet).apply {
            state = BottomSheetBehavior.STATE_HALF_EXPANDED
            halfExpandedRatio = SHEET_HALF_RATIO

            isFitToContents = false
            isHideable = false
        }.apply {
            bottomSheetCallback = HomeBottomSheetCallback(this).also {
                bottomSheetCallback = it
            }
        }
    }

    private fun setupTopPanelBehaviour() {
        val params = binding.homeTopPanel.layoutParams as CoordinatorLayout.LayoutParams
        topPanelBehavior = params.behavior as TopPanelBehaviour
    }

    private fun setupButtonsListeners() {
        binding.homeStartButton.setOnClickListener {
            startMapInteraction()
        }
        binding.homeBackButton.setOnClickListener {
            endMapInteraction()
        }
        binding.homeMarkerButton.setOnClickListener {
            walkingMapProvider.mapController.createMarker()
        }
    }

    private fun startMapInteraction() {
        removeCategoriesListener()

        topPanelBehavior?.togglePanelState(binding.homeTopPanel, show = false)
        bottomSheetBehavior?.state = BottomSheetBehavior.STATE_COLLAPSED
        bottomSheetBehavior?.isDraggable = true

        toggleMapState(willInteraction = true)
        viewModel.toggleInteractionMode(true)
    }

    private fun endMapInteraction() {
        setupCategoriesListener()

        topPanelBehavior?.togglePanelState(binding.homeTopPanel, show = true)
        bottomSheetBehavior?.state = BottomSheetBehavior.STATE_HALF_EXPANDED
        bottomSheetBehavior?.isDraggable = false

        toggleMapState(willInteraction = false)
        viewModel.toggleInteractionMode(false)
    }

    private fun setupCategoriesListener() {
        binding.homeCategoriesWidget.setStateListener(viewModel::categoriesStateSelected)
    }

    private fun removeCategoriesListener() {
        binding.homeCategoriesWidget.setStateListener {}
    }

    private fun toggleMapState(willInteraction: Boolean) {
        if (!willInteraction) {
            walkingMapProvider.mapController.restoreMap()
            return
        }

        walkingMapProvider.mapController.setCenterMarkerVisibility(true)
        walkingMapProvider.mapController.toggleCreateMarkerFeature(true)
    }

    override fun consumeSideEffect(effect: SideEffect) = Unit

    private companion object {
        const val SHEET_HALF_RATIO = .5f
    }
}
