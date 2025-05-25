@file:Suppress("MagicNumber")

package ru.pasha.feature.home.internal.view

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import android.util.AttributeSet
import android.view.Gravity
import android.view.animation.CycleInterpolator
import android.view.animation.TranslateAnimation
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.res.ResourcesCompat
import ru.pasha.common.extensions.dpToPx
import ru.pasha.common.extensions.dpToPxF
import ru.pasha.feature.home.R

class FeedbackView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {

    interface FeedbackListener {
        fun onSubmit(rating: Int, comment: String)
        fun onCancel()
    }

    private var listener: FeedbackListener? = null
    private var rating = 0
    private val stars = mutableListOf<ImageView>()
    private lateinit var etComment: EditText

    private val starContainer = LinearLayout(context).apply {
        orientation = HORIZONTAL
        gravity = Gravity.CENTER
    }

    init {
        orientation = VERTICAL
        setupView()
        setBackgroundColor(
            ResourcesCompat.getColor(
                resources,
                ru.pasha.common.R.color.walking_app_night_500,
                context.theme
            )
        )
    }

    fun setListener(listener: FeedbackListener) {
        this.listener = listener
    }

    private fun setupView() {
        TextView(context).apply {
            text = "Оцените маршрут"
            setTextColor(Color.parseColor("#2D3748"))
            textSize = 20f
            gravity = Gravity.CENTER
            addView(this, LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT)
//            updateLayoutParams<MarginLayoutParams> {
//                updateMargins(bottom =  16.dpToPx)
//            }
        }
        addView(starContainer, LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT)

        repeat(5) { index ->
            ImageView(context).apply {
                setImageResource(R.drawable.ic_star_outline)
                layoutParams = LayoutParams(40.dpToPx, 40.dpToPx).apply {
                    marginEnd = 4.dpToPx
                }
                setOnClickListener { setRating(index + 1) }
                stars.add(this)
                starContainer.addView(this)
            }
        }

        etComment = EditText(context).apply {
            hint = "Ваш комментарий..."
            background = createInputBackground()
            setPadding(16.dpToPx, 12.dpToPx, 16.dpToPx, 12.dpToPx)
            gravity = Gravity.TOP
        }
        addView(etComment, LayoutParams.MATCH_PARENT, 120.dpToPx).apply {
//            updateLayoutParams<MarginLayoutParams> {
//                updateMargins(top = 16.dpToPx, bottom = 24.dpToPx)
//            }
        }

        val buttonContainer = LinearLayout(context).apply {
            orientation = HORIZONTAL
            gravity = Gravity.END
        }
        addView(buttonContainer, LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT)

        Button(context).apply {
            text = "Отмена"
            setTextColor(Color.parseColor("#4A5568"))
            background = null
            setOnClickListener { listener?.onCancel() }
            buttonContainer.addView(this)
        }

        Button(context).apply {
            text = "Отправить"
            setTextColor(Color.WHITE)
            background = createSubmitButtonBackground()
            setOnClickListener { handleSubmit() }
            buttonContainer.addView(this, LayoutParams(120.dpToPx, 48.dpToPx).apply {
                marginStart = 16.dpToPx
            })
        }
    }

    private fun setRating(newRating: Int) {
        rating = newRating
        stars.forEachIndexed { index, imageView ->
            val resId =
                if (index < rating) R.drawable.ic_star_filled else R.drawable.ic_star_outline
            imageView.setImageResource(resId)
            imageView.animate().scaleX(1.2f).scaleY(1.2f).setDuration(200).withEndAction {
                imageView.animate().scaleX(1f).scaleY(1f).duration = 100
            }
        }
    }

    private fun handleSubmit() {
        if (rating == 0) {
            animateError()
            return
        }
        listener?.onSubmit(rating, etComment.text.toString())
    }

    private fun animateError() {
        val shake = TranslateAnimation(0f, 30f, 0f, 0f).apply {
            duration = 300
            interpolator = CycleInterpolator(3f)
        }
        starContainer.startAnimation(shake)
    }

    private fun createInputBackground(): Drawable {
        val shape = GradientDrawable()
        shape.cornerRadius = 8.dpToPxF
        shape.setStroke(2.dpToPx, Color.parseColor("#E2E8F0"))
        return shape
    }

    private fun createSubmitButtonBackground(): Drawable {
        val shape = GradientDrawable()
        shape.cornerRadius = 24.dpToPxF
        shape.setColor(Color.parseColor("#48BB78"))
        return shape
    }
}
