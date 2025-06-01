package ru.pasha.feature.home.internal.view

import android.animation.Animator
import android.animation.ValueAnimator
import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.animation.doOnEnd
import ru.pasha.feature.home.R

internal class TopPanelBehaviour(
    context: Context,
    attrs: AttributeSet? = null
) : CoordinatorLayout.Behavior<TopPanelView>(context, attrs) {

    private val bottomSheetOverlap = context.resources
        .getDimension(R.dimen.walking_app_home_bottom_sheet_corner_radius)

    private var stateChangeAnimator: Animator? = null

    private var inAnimation = false

    private var initialHeight: Int? = null

    private var showing: Boolean? = null

    override fun layoutDependsOn(
        parent: CoordinatorLayout,
        child: TopPanelView,
        dependency: View
    ): Boolean {
        return dependency is HomeBottomSheetView
    }

    override fun onDependentViewChanged(
        parent: CoordinatorLayout,
        child: TopPanelView,
        dependency: View
    ): Boolean {
        if (initialHeight == null) {
            val newHeight = dependency.top + bottomSheetOverlap.toInt()
            initialHeight = newHeight

//            if (showing == true) {
            child.layoutParams.height = newHeight.coerceAtLeast(0)
            child.requestLayout()
//            }
        }
        return true
    }

    override fun onDetachedFromLayoutParams() {
        super.onDetachedFromLayoutParams()

        stateChangeAnimator?.cancel()
        stateChangeAnimator = null
    }

    fun togglePanelState(panel: TopPanelView, show: Boolean) {
        if (inAnimation) return
        showing = show

        if (initialHeight == null) {
            panel.layoutParams.height = 0
            panel.requestLayout()
            return
        }

        val targetValue = if (show) initialHeight else 0f

        inAnimation = true
        stateChangeAnimator = ValueAnimator.ofFloat(panel.height.toFloat(), targetValue!!.toFloat()).apply {
            doOnEnd { inAnimation = false }
            addUpdateListener {
                val newValue = it.animatedValue as Float
                panel.layoutParams.height = newValue.toInt()
                panel.requestLayout()
            }
            duration = SLIDE_ANIMATION_DURATION
        }.also { it.start() }
    }

    private companion object {
        const val SLIDE_ANIMATION_DURATION = 300L
    }
}
