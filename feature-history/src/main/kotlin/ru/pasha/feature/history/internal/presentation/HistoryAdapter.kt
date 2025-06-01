package ru.pasha.feature.history.internal.presentation

import androidx.core.view.isVisible
import ru.pasha.common.views.BaseRecyclerAdapter
import ru.pasha.feature.history.R
import ru.pasha.feature.history.databinding.HistoryItemBinding
import ru.pasha.feature.history.internal.domain.PreviewEntity

internal class HistoryAdapter(
    private val downloadCallback: (PreviewEntity) -> Unit,
    private val removeLocalCallback: (PreviewEntity) -> Unit,
    private val openCallback: (PreviewEntity) -> Unit,
) : BaseRecyclerAdapter<HistoryItemBinding, PreviewEntity>(
    inflateBinding = { inflater, parent, attach ->
        HistoryItemBinding.inflate(inflater, parent, attach)
    }
) {
    override fun bind(binding: HistoryItemBinding, item: PreviewEntity, position: Int?) {
        binding.routeName.text = item.name

        setOnItemClickListener {
            openCallback(item)
        }

        binding.progressBar.isVisible = false
        binding.routeButton.isVisible = true

        binding.routeButton.setImageResource(
            if (item.downloaded) R.drawable.ic_delete_24 else R.drawable.ic_cloud_download_24
        )

        binding.routeButton.setOnClickListener {
            binding.routeButton.isVisible = false
            binding.progressBar.isVisible = true

            val delay = 5000L
            binding.routeButton.postDelayed(
                {
                    binding.progressBar.isVisible = false
                    binding.routeButton.isVisible = true
                },
                delay
            )

            if (item.downloaded) {
                removeLocalCallback(item)
            } else {
                downloadCallback(item)
            }
        }
    }
}
