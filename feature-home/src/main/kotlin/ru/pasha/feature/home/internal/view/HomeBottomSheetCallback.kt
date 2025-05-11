package ru.pasha.feature.home.internal.view

import android.view.View
import com.google.android.material.bottomsheet.BottomSheetBehavior

internal class HomeBottomSheetCallback(
    private val behavior: BottomSheetBehavior<HomeBottomSheetView>
) : BottomSheetBehavior.BottomSheetCallback() {
    override fun onStateChanged(bottomSheet: View, newState: Int) {
        val wasExpanded = behavior.state == BottomSheetBehavior.STATE_EXPANDED
        val wasCollapsed = behavior.state == BottomSheetBehavior.STATE_COLLAPSED

        val willExpanded = newState == BottomSheetBehavior.STATE_EXPANDED
        val willCollapsed = newState == BottomSheetBehavior.STATE_COLLAPSED

        behavior.state = when {
            wasCollapsed && willExpanded -> BottomSheetBehavior.STATE_HALF_EXPANDED
            wasExpanded && willCollapsed -> BottomSheetBehavior.STATE_HALF_EXPANDED
            else -> newState
        }
    }

    override fun onSlide(bottomSheet: View, slideOffset: Float) = Unit
}
