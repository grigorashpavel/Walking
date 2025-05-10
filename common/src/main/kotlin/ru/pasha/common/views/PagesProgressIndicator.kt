package ru.pasha.common.views

import android.animation.Animator
import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import android.view.View
import android.view.animation.LinearInterpolator
import ru.pasha.common.R
import ru.pasha.common.extensions.dpToPxF

class PagesProgressIndicator @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private var activeColor = Color.TRANSPARENT
    private var inactiveColor = Color.TRANSPARENT
    private var itemSpacing = 8.dpToPxF
    private var itemRadius = 8.dpToPxF
    private var activeWidth = 0f

    private var itemsCount = 0
    private var currentPosition = 0
    private var progress = 0f
    private val paint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val rect = RectF()

    private var progressAnimator: Animator? = null
    private var tickDuration = 5000L

    init {
        context.theme.obtainStyledAttributes(
            attrs,
            R.styleable.PagesProgressIndicator,
            0, 0
        ).apply {
            try {
                activeColor = getColor(R.styleable.PagesProgressIndicator_activeColor, Color.RED)
                inactiveColor = getColor(R.styleable.PagesProgressIndicator_inactiveColor, Color.GRAY)
                itemRadius = getDimension(R.styleable.PagesProgressIndicator_itemRadius, 8f.dpToPxF)
                itemSpacing = getDimension(R.styleable.PagesProgressIndicator_itemSpacing, 8f.dpToPxF)
            } finally {
                recycle()
            }
        }
    }

    fun setCount(count: Int) {
        itemsCount = count
        requestLayout()
        invalidate()
    }

    fun setPosition(position: Int, animate: Boolean = true, tickDuration: Long) {
        cancelProgress()
        currentPosition = position
        this.tickDuration = tickDuration
        progress = 0f

        if (animate) {
            progressAnimator = ValueAnimator.ofFloat(0f, 1f).apply {
                duration = tickDuration
                interpolator = LinearInterpolator()
                addUpdateListener {
                    progress = it.animatedValue as Float
                    invalidate()
                }
                start()
            }
        } else {
            progress = 1f
        }
        invalidate()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val minHeight = (itemRadius * 2 + paddingTop + paddingBottom).toInt()
        val height = resolveSize(minHeight, heightMeasureSpec)
        setMeasuredDimension(getDefaultSize(suggestedMinimumWidth, widthMeasureSpec), height)
    }

    override fun onDraw(canvas: Canvas) {
        if (itemsCount == 0) return

        val totalWidth = width - paddingStart - paddingEnd
        val itemHeight = height - paddingTop - paddingBottom.toFloat()

        activeWidth = totalWidth * if (itemsCount > 1) ACTIVE_WIDTH_COEFFICIENT else 1f
        val inactiveWidth = (totalWidth - activeWidth - itemSpacing * (itemsCount - 1)) / (itemsCount - 1)

        var currentX = paddingStart.toFloat()

        for (i in 0 until itemsCount) {
            val isActive = i == currentPosition

            if (isActive) {
                paint.color = inactiveColor
                rect.set(currentX, paddingTop.toFloat(), currentX + activeWidth, paddingTop + itemHeight)
                canvas.drawRoundRect(rect, itemRadius, itemRadius, paint)

                paint.color = activeColor
                val progressWidth = activeWidth * progress
                rect.set(currentX, paddingTop.toFloat(), currentX + progressWidth, paddingTop + itemHeight)
                canvas.drawRoundRect(rect, itemRadius, itemRadius, paint)

                currentX += activeWidth + itemSpacing
            } else {
                paint.color = inactiveColor
                rect.set(currentX, paddingTop.toFloat(), currentX + inactiveWidth, paddingTop + itemHeight)
                canvas.drawRoundRect(rect, itemRadius, itemRadius, paint)

                currentX += inactiveWidth + itemSpacing
            }
        }
    }

    override fun onDetachedFromWindow() {
        cancelProgress()
        super.onDetachedFromWindow()
    }

    fun cancelProgress() {
        progressAnimator?.cancel()
        progressAnimator = null
    }

    private companion object {
        const val ACTIVE_WIDTH_COEFFICIENT = 0.7f
    }
}
