package ru.pasha.feature.home.internal.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.view.WindowInsetsCompat
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

internal class HomeFragment @Inject constructor(
    private val viewModelFactory: HomeViewModel.Factory,
    private val walkingMapProvider: WalkingMapProvider
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

    override fun onResume() {
        super.onResume()
        cicerone.getNavigatorHolder().setNavigator(navigation)
    }

    override fun onPause() {
        super.onPause()
        cicerone.getNavigatorHolder().removeNavigator()
    }

    override fun onDetach() {
        super.onDetach()
        cicerone.router.exit()
    }

    override fun createViewModel(): HomeViewModel = viewModelFactory.create()

    override fun onApplyInsets(insets: WindowInsetsCompat): WindowInsetsCompat {
        return insets
    }

    override fun onDestroyView() {
        bottomSheetCallback?.let { bottomSheetBehavior?.removeBottomSheetCallback(it) }
        bottomSheetBehavior = null
        bottomSheetCallback = null

        topPanelBehavior = null
        super.onDestroyView()
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
            binding.homeCategoriesWidget.setStateListener {}
            topPanelBehavior?.togglePanelState(binding.homeTopPanel, show = false)
            bottomSheetBehavior?.isDraggable = true
        }
        binding.homeBackButton.setOnClickListener {
            topPanelBehavior?.togglePanelState(binding.homeTopPanel, show = true)
            bottomSheetBehavior?.state = BottomSheetBehavior.STATE_HALF_EXPANDED
            bottomSheetBehavior?.isDraggable = false
            setupCategoriesListener()
        }
    }

    private fun setupCategoriesListener() {
        binding.homeCategoriesWidget.setStateListener(viewModel::categoriesStateSelected)
    }

    override fun render(viewState: HomeViewState) {
        binding.homeCategoriesWidget.setState(viewState.categoryState)
    }

    override fun consumeSideEffect(effect: SideEffect) = Unit

    private companion object {
        const val SHEET_HALF_RATIO = .5f
    }
}
