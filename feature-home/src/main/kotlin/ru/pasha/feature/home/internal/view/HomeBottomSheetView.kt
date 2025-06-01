package ru.pasha.feature.home.internal.view

import android.content.Context
import android.os.Parcelable
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetBehavior
import kotlinx.parcelize.Parcelize
import ru.pasha.common.Text
import ru.pasha.common.map.Marker
import ru.pasha.feature.home.databinding.HomeBottomSheetViewBinding
import ru.pasha.feature.home.internal.presentation.MarkersAdapter
import ru.pasha.feature.home.internal.presentation.PreviewMarkersAdapter

internal class HomeBottomSheetView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
) : FrameLayout(context, attrs, defStyleAttr), RemoveListener {
    private val behaviour by lazy { BottomSheetBehavior.from(this) }

    private var horizontalAdapter: PreviewMarkersAdapter? = null
    private var verticalAdapter: MarkersAdapter? = null

    private val binding = HomeBottomSheetViewBinding.inflate(
        LayoutInflater.from(context), this, true
    )

    private var removeAllCallback: () -> Unit = {}
    private var removeMarkerCallback: (Marker) -> Unit = {}

    init {
        binding.homeMarkersPreviewRecyclerView.adapter =
            PreviewMarkersAdapter().also { horizontalAdapter = it }
        binding.homeMarkersPreviewRecyclerView.layoutManager = LinearLayoutManager(context).apply {
            orientation = LinearLayoutManager.HORIZONTAL
        }

        binding.homeMarkersRecyclerView.adapter = MarkersAdapter(this).also { verticalAdapter = it }
        binding.homeMarkersRecyclerView.layoutManager = LinearLayoutManager(context).apply {
            orientation = LinearLayoutManager.VERTICAL
        }

        binding.homeRemoveMarkersButton.setOnClickListener {
            removeAllCallback()
        }

        binding.homeMenuHistoryItem.setOnClickListener {
        }
        binding.homeMenuSettingsItem.setOnClickListener {
        }
    }

    fun render(state: State, onRemoveMarkers: () -> Unit, onRemoveMarker: (Marker) -> Unit) {
        removeAllCallback = onRemoveMarkers
        removeMarkerCallback = onRemoveMarker

        binding.progressBar.isIndeterminate = state.isLoading

        val hasMarker = state.markers.isNotEmpty()

        binding.homeMarkersPreviewRecyclerView.isVisible = hasMarker
        binding.homeMarkersRecyclerView.isVisible = hasMarker

        binding.homeRemoveMarkersButton.isVisible = hasMarker

        horizontalAdapter?.updateItems(state.markers, forceUpdate = false)
        verticalAdapter?.updateItems(state.markers, forceUpdate = true)

        if (state.routeLength == null) {
            binding.homeRouteLengthTitle.setText("")
            binding.homeRouteLengthTitle.isVisible = false
        } else {
            binding.homeRouteLengthTitle.setText(
                "Длинна маршрута: ${state.routeLength} м."
            )
            binding.homeRouteLengthTitle.isVisible = true
        }

        binding.homeMenuHistoryItem.isVisible = state.isMenuVisible
        binding.homeMenuSettingsItem.isVisible = state.isMenuVisible
    }

    override fun remove(marker: Marker) {
        removeMarkerCallback(marker)
    }

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

    override fun onDetachedFromWindow() {
        horizontalAdapter = null
        super.onDetachedFromWindow()
    }

    data class State(
        val markers: List<Marker>,
        val isLoading: Boolean,
        val isMenuVisible: Boolean,
        val inRouteTime: Text?,
        val routeLength: Double?,
    )

    @Parcelize
    private data class ExpandedSavedState(
        val peekHeight: Int,
        val expandedOffset: Int,
        val parentSavedState: Parcelable?,
    ) : Parcelable
}

internal interface RemoveListener {
    fun remove(marker: Marker)
}
