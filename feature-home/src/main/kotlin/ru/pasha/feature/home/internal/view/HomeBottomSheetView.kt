package ru.pasha.feature.home.internal.view

import android.content.Context
import android.os.Parcelable
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import com.google.android.material.bottomsheet.BottomSheetBehavior
import kotlinx.parcelize.Parcelize
import ru.pasha.feature.home.databinding.HomeBottomSheetViewBinding

internal class HomeBottomSheetView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
) : FrameLayout(context, attrs, defStyleAttr) {
    private val behaviour by lazy { BottomSheetBehavior.from(this) }

    private val binding = HomeBottomSheetViewBinding.inflate(
        LayoutInflater.from(context), this, true
    )

    override fun onSaveInstanceState(): Parcelable = ExpandedSavedState(
        peekHeight = behaviour.peekHeight,
        expandedOffset = behaviour.expandedOffset,
        parentSavedState = super.onSaveInstanceState()
    )

    override fun onRestoreInstanceState(state: Parcelable?) {
        val expandedState = state as? ExpandedSavedState ?: return
        with(expandedState) {
            behaviour.peekHeight = peekHeight
            behaviour.expandedOffset = expandedOffset
            super.onRestoreInstanceState(parentSavedState)
        }
    }

    @Parcelize
    private data class ExpandedSavedState(
        val peekHeight: Int,
        val expandedOffset: Int,
        val parentSavedState: Parcelable?,
    ) : Parcelable
}
