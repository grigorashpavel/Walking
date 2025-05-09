package ru.pasha.feature.banner.internal.presentation

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ru.pasha.common.format
import ru.pasha.common.loadImage
import ru.pasha.feature.banner.databinding.ItemBannerBinding
import ru.pasha.feature.banner.internal.domain.BannerEntity

class BannerAdapter : RecyclerView.Adapter<BannerViewHolder>() {

    private val items = mutableListOf<BannerEntity>()

    @SuppressLint("NotifyDataSetChanged")
    fun submitList(list: List<BannerEntity>) {
        items.clear()
        items.addAll(list)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BannerViewHolder {
        val binding = ItemBannerBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return BannerViewHolder(binding)
    }

    override fun onBindViewHolder(holder: BannerViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount() = items.size
}

class BannerViewHolder(
    private val binding: ItemBannerBinding
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(item: BannerEntity) = with(binding) {
        bannerItemImage.loadImage(item.image)
        item.title.let { bannerItemTitle.text = it.format(root.context) }
        item.subtitle?.let { bannerItemSubtitle.text = it.format(root.context) }
    }
}
