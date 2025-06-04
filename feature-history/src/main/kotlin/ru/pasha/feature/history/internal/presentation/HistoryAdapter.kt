package ru.pasha.feature.history.internal.presentation

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import ru.pasha.common.views.BaseRecyclerAdapter
import ru.pasha.common.views.BaseViewHolder
import ru.pasha.feature.history.R
import ru.pasha.feature.history.databinding.HistoryItemBinding
import ru.pasha.feature.history.internal.domain.PreviewEntity

internal class HistoryAdapter(
    private val downloadCallback: (PreviewEntity) -> Unit,
    private val removeLocalCallback: (PreviewEntity) -> Unit,
    private val openCallback: (PreviewEntity) -> Unit,
    private val loadMore: () -> Unit,
) : BaseRecyclerAdapter<HistoryItemBinding, PreviewEntity?>(
    inflateBinding = { inflater, parent, attach ->
        HistoryItemBinding.inflate(inflater, parent, attach)
    }
) {
    override fun bind(binding: HistoryItemBinding, item: PreviewEntity?, position: Int?) {
        if (item == null) return

        binding.routeName.text = item.name

        binding.root.setOnClickListener {
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

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): BaseViewHolder<HistoryItemBinding, PreviewEntity?> {
        return if (viewType == ITEM) {
            super.onCreateViewHolder(parent, viewType)
        } else {
            val inflater = LayoutInflater.from(parent.context)
            val binding = HistoryItemBinding.inflate(inflater, parent, false)
            return object : BaseViewHolder<HistoryItemBinding, PreviewEntity?>(binding) {
                override fun bind(item: PreviewEntity?, position: Int?) {
                    binding.progressBar.isVisible = false
                    binding.routeButton.isVisible = false
                    binding.icon.isVisible = false
                    binding.routeName.isVisible = false
                    binding.footerButton.isVisible = true
                    binding.footerButton.setOnClickListener {
                        loadMore()
                    }
                }
            }
        }
    }

    override fun onBindViewHolder(
        holder: BaseViewHolder<HistoryItemBinding, PreviewEntity?>,
        position: Int,
        payloads: MutableList<Any>
    ) {
        if (position < super.getItemCount()) {
            super.onBindViewHolder(holder, position, payloads)
        } else {
            holder.bind(null, position)
        }
    }

    override fun getItemCount(): Int {
        return super.getItemCount() + 1
    }

    override fun getItemViewType(position: Int): Int =
        if (position == super.getItemCount()) FOOTER else ITEM

    companion object {
        private const val ITEM = 0
        private const val FOOTER = 1
    }
}
