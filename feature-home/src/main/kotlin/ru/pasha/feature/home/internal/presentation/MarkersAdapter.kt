package ru.pasha.feature.home.internal.presentation

import ru.pasha.common.Text
import ru.pasha.common.format
import ru.pasha.common.map.Marker
import ru.pasha.common.views.BaseRecyclerAdapter
import ru.pasha.feature.home.R
import ru.pasha.feature.home.databinding.BigMarkerItemBinding
import ru.pasha.feature.home.internal.view.RemoveListener

internal class MarkersAdapter(
    private val removeListener: RemoveListener,
) : BaseRecyclerAdapter<BigMarkerItemBinding, Marker>(
    inflateBinding = { inflater, parent, attach ->
        BigMarkerItemBinding.inflate(inflater, parent, attach)
    }
) {
    override fun bind(binding: BigMarkerItemBinding, item: Marker, position: Int?) {
        binding.bigMarkerItemIcon.setColorFilter(item.color)
        binding.bigItemMarkerText.text =
            Text.Resource(ru.pasha.common.R.string.walking_app_poi_num)
                .format(binding.bigMarkerItemIcon.context)
                .toString()
                .format(position?.let { it + 1 })

        binding.bigMarkerItemDeleteButton.setOnClickListener {
            removeListener.remove(item)
        }
    }
}
