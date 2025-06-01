package ru.pasha.feature.home.internal.view

import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.appcompat.widget.AppCompatButton
import androidx.coordinatorlayout.widget.CoordinatorLayout
import ru.pasha.feature.home.R

internal class StartButtonBehaviour(
    context: Context,
    attrs: AttributeSet? = null
) : CoordinatorLayout.Behavior<AppCompatButton>(context, attrs) {

    override fun layoutDependsOn(
        parent: CoordinatorLayout,
        child: AppCompatButton,
        dependency: View
    ): Boolean = dependency.id == R.id.homeTopPanel

    override fun onDependentViewChanged(
        parent: CoordinatorLayout,
        child: AppCompatButton,
        dependency: View
    ): Boolean {
        val childHalf = child.height / 2
        val progress = 1f - dependency.bottom / dependency.height.toFloat()

        child.y = dependency.bottom - childHalf * (1 + progress)
        child.visibility = if (progress >= 1f) View.GONE else View.VISIBLE

        return true
    }
}
