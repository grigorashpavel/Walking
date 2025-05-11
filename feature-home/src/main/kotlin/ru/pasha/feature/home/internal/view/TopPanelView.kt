package ru.pasha.feature.home.internal.view

import android.content.Context
import android.util.AttributeSet
import androidx.constraintlayout.widget.ConstraintLayout

internal class TopPanelView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
) : ConstraintLayout(context, attrs, defStyleAttr) {

    var localHeight: Int? = null
        private set

//    override fun onSaveInstanceState(): Parcelable? {
//        return localHeight?.let { h ->
//            PanelSavedState(
//                height = h,
//                parentSavedState = super.onSaveInstanceState(),
//            )
//        } ?: super.onSaveInstanceState()
//    }

//    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
//        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
//
//        val measuredWidth = MeasureSpec.getSize(widthMeasureSpec)
//        val measuredHeight = localHeight ?: MeasureSpec.getSize(heightMeasureSpec)
//
//        setMeasuredDimension(measuredWidth, measuredHeight)
//    }

//    override fun onRestoreInstanceState(state: Parcelable?) {
//        val savedState = state as? PanelSavedState
//        this.localHeight = savedState?.height
//
//        super.onRestoreInstanceState(savedState?.parentSavedState ?: state)
//    }
//
//    @Parcelize
//    private data class PanelSavedState(
//        val height: Int,
//        val parentSavedState: Parcelable?,
//    ) : Parcelable
}
