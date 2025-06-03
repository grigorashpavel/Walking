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
import ru.pasha.common.format
import ru.pasha.common.map.Marker
import ru.pasha.common.map.Route
import ru.pasha.feature.home.R
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

    private var navigateToHistory: () -> Unit = {}
    private var navigateToSettings: () -> Unit = {}

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
            navigateToHistory()
        }
        binding.homeMenuSettingsItem.setOnClickListener {
            navigateToSettings()
        }
    }

    fun setListeners(
        onRemoveMarkers: () -> Unit,
        onRemoveMarker: (Marker) -> Unit,
        navigateToHistory: () -> Unit,
        navigateToSettings: () -> Unit,
    ) {
        removeAllCallback = onRemoveMarkers
        removeMarkerCallback = onRemoveMarker
        this.navigateToHistory = navigateToHistory
        this.navigateToSettings = navigateToSettings
    }

    fun render(state: State) {
        binding.progressBar.isIndeterminate = state.isLoading

        val hasMarker = state.markers.isNotEmpty()
        val hasRoute = state.route != null

        binding.homeMarkersPreviewRecyclerView.isVisible = hasMarker && !hasRoute
        binding.homeMarkersRecyclerView.isVisible = hasMarker && !hasRoute
        binding.homeRemoveMarkersButton.isVisible = hasMarker && !hasRoute

        horizontalAdapter?.updateItems(state.markers, forceUpdate = false)
        verticalAdapter?.updateItems(state.markers, forceUpdate = true)

        binding.homeRouteTimerText.isVisible = state.inRouteTime != null

        if (state.route != null) {
            val len = String.format("%.2f", state.route.length)
            binding.homeRouteLengthTitle.text = Text.Resource(ru.pasha.common.R.string.walking_app_route_length)
                .format(context)
                .toString()
                .format(len)
            binding.homeRouteLengthTitle.isVisible = true
        } else {
            binding.homeRouteLengthTitle.isVisible = false
            binding.homeRouteLengthTitle.text = ""
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
        val route: Route?,
        val inRouteTime: Text?,
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
