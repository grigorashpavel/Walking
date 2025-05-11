package ru.pasha.feature.home.internal.view

import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.coordinatorlayout.widget.CoordinatorLayout
import ru.pasha.common.R

internal class TopButtonsBehaviour(
    context: Context,
    attrs: AttributeSet? = null
) : CoordinatorLayout.Behavior<View>(context, attrs) {
    private var minY = context.resources.getDimension(R.dimen.walking_app_notification_bar_height)

    override fun layoutDependsOn(parent: CoordinatorLayout, child: View, dependency: View): Boolean =
        dependency is HomeBottomSheetView

    override fun onDependentViewChanged(parent: CoordinatorLayout, child: View, dependency: View): Boolean {
        child.y = (dependency.top.toFloat() - child.height).coerceAtLeast(minY)

        return true
    }
}
