@file:Suppress("MagicNumber")

package ru.pasha.feature.home.internal.view

import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
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
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.setPadding
import androidx.core.view.updateLayoutParams
import androidx.core.view.updateMargins
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import ru.pasha.common.R
import ru.pasha.common.Text
import ru.pasha.common.extensions.dpToPx
import ru.pasha.common.extensions.dpToPxF
import ru.pasha.common.format

class RouteNameDialog : DialogFragment() {
    private lateinit var feedbackView: RouteNameView

    interface Callback {
        fun onSubmit(name: String)
        fun onOpened()
        fun onCancel()
    }

    var callback: Callback? = null

    var correct = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        callback?.onOpened()
        feedbackView = RouteNameView(requireContext()).apply {
            setListener(object : RouteNameView.Listener {
                override fun onSubmit(name: String) {
                    correct = true
                    callback?.onSubmit(name)
                    dismiss()
                }

                override fun onCancel() {
                    if (!correct) {
                        callback?.onCancel()
                        dismiss()
                    }
                }
            })
        }
        return feedbackView
    }

    override fun onDismiss(dialog: DialogInterface) {
        if (!correct) callback?.onCancel()
        super.onDismiss(dialog)
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
            RouteNameDialog().apply {
                this.callback = callback
            }.show(manager, TAG)
        }
    }
}

class RouteNameView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {

    interface Listener {
        fun onSubmit(name: String)
        fun onCancel()
    }

    private var listener: Listener? = null
    private lateinit var etComment: EditText

    private val titleContainer = LinearLayout(context).apply {
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

    fun setListener(listener: Listener) {
        this.listener = listener
    }

    private fun setupView() {
        TextView(context).apply {
            text = Text.Resource(R.string.walking_app_route_name_title).format(context)
            setTextColor(resources.getColor(R.color.walking_app_night_900))
            textSize = 20f
            gravity = Gravity.CENTER
            addView(this, LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT)
        }
        addView(titleContainer, LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT)

        etComment = EditText(context).apply {
            this.hint = Text.Resource(R.string.walking_app_route_name_hint).format(context)
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

    private fun handleSubmit() {
        if (etComment.text.isBlank()) {
            animateErrorText()
            return
        }
        listener?.onSubmit(etComment.text.toString())
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
        fun show(context: Context, callback: RouteNameDialog.Callback) {
            val manager = (context as? androidx.fragment.app.FragmentActivity)
                ?.supportFragmentManager
                ?: throw IllegalArgumentException("Context must be FragmentActivity")

            RouteNameDialog.show(manager, callback)
        }
    }
}
