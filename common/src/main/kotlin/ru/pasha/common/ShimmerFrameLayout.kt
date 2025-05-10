package ru.pasha.common

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import ru.pasha.common.extensions.dpToPxF

class ShimmerFrameLayout @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    private var autoStart: Boolean = true

    init {
        context.obtainStyledAttributes(attrs, R.styleable.ShimmerFrameLayout).apply {
            autoStart = getBoolean(
                R.styleable.ShimmerFrameLayout_shimmerAutoStart, true
            )
            recycle()
        }

        elevation = 4.dpToPxF
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        if (autoStart) { startShimmer() }
    }

    fun startShimmer() {
        forEachChild { if (it is ShimmerView) it.startShimmer() }
    }

    fun stopShimmer() {
        forEachChild { if (it is ShimmerView) it.stopShimmer() }
    }

    private inline fun forEachChild(action: (View) -> Unit) {
        for (i in 0..<childCount) {
            action(getChildAt(i))
        }
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        stopShimmer()
    }
}
