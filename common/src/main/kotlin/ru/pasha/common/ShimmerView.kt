package ru.pasha.common

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.BlurMaskFilter
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.LinearGradient
import android.graphics.Matrix
import android.graphics.Paint
import android.graphics.Shader
import android.util.AttributeSet
import android.view.View
import android.view.animation.LinearInterpolator
import ru.pasha.common.extensions.dpToPxF
import kotlin.math.cos
import kotlin.math.max
import kotlin.math.min
import kotlin.math.sin

class ShimmerView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private val paint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val backgroundPaint = Paint(Paint.ANTI_ALIAS_FLAG)
    private var animator: ValueAnimator? = null
    private val gradientMatrix = Matrix()
    private var gradient: LinearGradient? = null

    private var tickDuration = 1500L
    private var angle: Float = 40f

    private var shimmerColor: Int = Color.RED
    private var shimmerIntensity: Float = 0.3f

    private var cornerRadius = 0f

    init {
        context.obtainStyledAttributes(attrs, R.styleable.ShimmerView).apply {
            shimmerColor = getColor(R.styleable.ShimmerView_shimmerColor, shimmerColor)
            shimmerIntensity = getFloat(R.styleable.ShimmerView_shimmerIntensity, shimmerIntensity)
            angle = getFloat(R.styleable.ShimmerView_angle, angle)
            cornerRadius = getDimension(R.styleable.ShimmerView_cornerRadius, cornerRadius).dpToPxF
            recycle()
        }

        backgroundPaint.color = shimmerColor
    }

    @Suppress("MagicNumber")
    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)

        val (dx, dy) = calculateDirectionVector()

        val baseHsv = FloatArray(3)
        Color.colorToHSV(shimmerColor, baseHsv)
        baseHsv.apply { this[1] = max(0f, this[1] - SATURATION_SCALE) }
        val shimmerHsv = baseHsv.copyOf().apply {
            this[2] = min(1f, this[2] + shimmerIntensity)
        }
        val mainShimmerColor = Color.HSVToColor(shimmerHsv)

        val colors = intArrayOf(
            Color.TRANSPARENT,
            adjustAlpha(mainShimmerColor, 0.4f),
            adjustAlpha(mainShimmerColor, 0.8f),
            adjustAlpha(mainShimmerColor, 0.4f),
            Color.TRANSPARENT
        )
        val positions = floatArrayOf(0f, 0.3f, 0.5f, 0.7f, 1f)

        gradient = LinearGradient(
            -w * 0.5f, 0f, w * 0.5f, 0f,
            colors,
            positions,
            Shader.TileMode.CLAMP
        ).apply {
            gradientMatrix.setRotate(angle)
            setLocalMatrix(gradientMatrix)
        }

        paint.shader = gradient
        paint.maskFilter = BlurMaskFilter(BLUR_RADIUS, BlurMaskFilter.Blur.INNER)

        setupAnimator(w, h, dx, dy)
    }

    override fun onDraw(canvas: Canvas) {
        canvas.drawRoundRect(0f, 0f, width.toFloat(), height.toFloat(), cornerRadius, cornerRadius, backgroundPaint)
        canvas.drawRoundRect(0f, 0f, width.toFloat(), height.toFloat(), cornerRadius, cornerRadius, paint)
    }

    override fun onDetachedFromWindow() {
        stopShimmer()
        super.onDetachedFromWindow()
    }

    fun startShimmer() {
        animator?.start()
    }

    fun stopShimmer() {
        animator?.cancel()
    }

    private fun setupAnimator(w: Int, h: Int, dx: Float, dy: Float) {
        animator?.cancel()

        animator = ValueAnimator.ofFloat(0f, 1f).apply {
            duration = tickDuration
            repeatCount = ValueAnimator.INFINITE
            interpolator = LinearInterpolator()

            addUpdateListener {
                val progress = it.animatedValue as Float
                gradientMatrix.setTranslate(
                    -w + progress * (w + dx),
                    progress * dy
                )
                gradient?.setLocalMatrix(gradientMatrix)
                invalidate()
            }
            start()
        }
    }

    private fun calculateDirectionVector(): Pair<Float, Float> {
        val radians = Math.toRadians(angle.toDouble())
        return Pair(
            (width * 2 * cos(radians)).toFloat(),
            (width * 2 * sin(radians)).toFloat()
        )
    }

    private fun adjustAlpha(color: Int, alphaFactor: Float): Int {
        val alpha = (Color.alpha(color) * alphaFactor).toInt()
        return Color.argb(
            alpha,
            Color.red(color),
            Color.green(color),
            Color.blue(color)
        )
    }

    private companion object {
        const val SATURATION_SCALE = .3f
        const val BLUR_RADIUS = 20f
    }
}
