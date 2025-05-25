package ru.pasha.feature.home.internal.presentation

import ru.pasha.common.map.Marker
import ru.pasha.common.views.BaseRecyclerAdapter
import ru.pasha.feature.home.databinding.MarkerItemBinding

class PreviewMarkersAdapter : BaseRecyclerAdapter<MarkerItemBinding, Marker>(
    inflateBinding = { inflater, parent, attach ->
        MarkerItemBinding.inflate(inflater, parent, attach)
    }
) {
    override fun bind(binding: MarkerItemBinding, item: Marker, position: Int?) {
        binding.itemMarkerImage.setColorFilter(item.color)
    }
}
