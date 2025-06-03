@file:Suppress("MagicNumber")

package ru.pasha.common.views

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.util.AttributeSet
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.view.animation.CycleInterpolator
import android.view.animation.TranslateAnimation
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.marginTop
import androidx.core.view.setPadding
import androidx.core.view.updateLayoutParams
import androidx.core.view.updateMargins
import androidx.core.view.updatePadding
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import ru.pasha.common.R
import ru.pasha.common.Text
import ru.pasha.common.extensions.dpToPx
import ru.pasha.common.extensions.dpToPxF
import ru.pasha.common.format

class FeedbackDialog : DialogFragment() {
    private lateinit var feedbackView: FeedbackView

    interface Callback {
        fun onSubmit(rating: Int, comment: String)
        fun onCancel()
    }

    var callback: Callback? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        feedbackView = FeedbackView(requireContext()).apply {
            setListener(object : FeedbackView.FeedbackListener {
                override fun onSubmit(rating: Int, comment: String) {
                    callback?.onSubmit(rating, comment)
                    dismiss()
                }

                override fun onCancel() {
                    callback?.onCancel()
                    dismiss()
                }
            })
        }
        return feedbackView
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.window?.apply {
            setBackgroundDrawableResource(android.R.color.transparent)
            setDimAmount(0.5f)
        }
        return dialog
    }

    companion object {
        private const val TAG = "FeedbackDialog"

        fun show(manager: FragmentManager, callback: Callback) {
            FeedbackDialog().apply {
                this.callback = callback
            }.show(manager, TAG)
        }
    }
}


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
        setPadding(16.dpToPx)
    }

    init {
        orientation = VERTICAL
        setupView()
        setBackgroundColor(
            ResourcesCompat.getColor(
                resources,
                R.color.walking_app_night_500,
                context.theme
            )
        )
        setPadding(
            26.dpToPx,
            36.dpToPx,
            26.dpToPx,
            36.dpToPx
        )
        setBackgroundResource(R.drawable.feedback_background)
    }

    fun setListener(listener: FeedbackListener) {
        this.listener = listener
    }

    private fun setupView() {
        TextView(context).apply {
            val text = if (isReport) {
                Text.Resource(R.string.walking_app_feedback_report)
            } else {
                Text.Resource(R.string.walking_app_rate_route)
            }

            this.text = text.format(context)
            setTextColor(resources.getColor(R.color.walking_app_night_900))
            textSize = 20f
            gravity = Gravity.CENTER
            addView(this, LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT)
        }
        addView(starContainer, LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT)

        val rateSize = if (isReport) 0 else 5
        repeat(rateSize) { index ->
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

        val hint = if (isReport) {
            Text.Resource(R.string.walking_app_feedback_report_hint)
        } else {
            Text.Resource(R.string.walking_app_your_comment)
        }
        etComment = EditText(context).apply {
            this.hint = hint.format(context)
            background = createInputBackground()
            setPadding(16.dpToPx, 12.dpToPx, 16.dpToPx, 12.dpToPx)
            gravity = Gravity.TOP
        }
        addView(etComment, LayoutParams.MATCH_PARENT, 164.dpToPx)
        etComment.updateLayoutParams<MarginLayoutParams> {
            updateMargins(left = 16.dpToPx, right = 20.dpToPx)
        }

        val buttonContainer = LinearLayout(context).apply {
            orientation = HORIZONTAL
            gravity = Gravity.END
            setPadding(16.dpToPx)
        }
        addView(buttonContainer, LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT)

        Button(context).apply {
            text = Text.Resource(ru.pasha.common.R.string.walking_app_your_comment_cancel)
                .format(context)
            setTextColor(resources.getColor(R.color.walking_app_night_900))
            background = null
            setOnClickListener { listener?.onCancel() }
            buttonContainer.addView(this)
        }

        Button(context).apply {
            text = Text.Resource(ru.pasha.common.R.string.walking_app_your_comment_send)
                .format(context)
            setTextColor(resources.getColor(R.color.walking_app_night_900))
            background = createSubmitButtonBackground()
            setOnClickListener { handleSubmit() }
            buttonContainer.addView(this, LayoutParams(120.dpToPx, 48.dpToPx).apply {
                marginStart = 16.dpToPx
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
        if (etComment.text.isBlank()) {
            animateErrorText()
            return
        } else if (!isReport && rating == 0) {
            animateErrorStars()
            return
        }
        listener?.onSubmit(rating, etComment.text.toString())
    }

    private fun animateErrorStars() {
        val shake = TranslateAnimation(0f, 30f, 0f, 0f).apply {
            duration = 300
            interpolator = CycleInterpolator(3f)
        }

        starContainer.startAnimation(shake)
    }

    private fun animateErrorText() {
        val shake = TranslateAnimation(0f, 30f, 0f, 0f).apply {
            duration = 300
            interpolator = CycleInterpolator(3f)
        }

        etComment.startAnimation(shake)
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
        shape.setColor(resources.getColor(R.color.walking_app_soft_green))
        return shape
    }

    companion object {
        private var isReport = false
        fun show(context: Context, isReport: Boolean, callback: FeedbackDialog.Callback) {
            this.isReport = isReport
            val manager = (context as? androidx.fragment.app.FragmentActivity)
                ?.supportFragmentManager
                ?: throw IllegalArgumentException("Context must be FragmentActivity")

            FeedbackDialog.show(manager, callback)
        }
    }
}
