package ru.pasha.common.views

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding

abstract class BaseViewHolder<B : ViewBinding, T>(binding: B) :
    RecyclerView.ViewHolder(binding.root) {
    abstract fun bind(item: T, position: Int? = null)
}

abstract class BaseRecyclerAdapter<B : ViewBinding, T>(
    private val items: MutableList<T> = mutableListOf(),
    private val inflateBinding: (LayoutInflater, ViewGroup, Boolean) -> B,
) : RecyclerView.Adapter<BaseViewHolder<B, T>>() {

    private var itemClickListener: ((T) -> Unit)? = null
    private var itemLongClickListener: ((T) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<B, T> {
        val inflater = LayoutInflater.from(parent.context)
        val binding = inflateBinding(inflater, parent, false)
        return object : BaseViewHolder<B, T>(binding) {
            override fun bind(item: T, position: Int?) {
                this@BaseRecyclerAdapter.bind(binding, item, position)
            }
        }.apply {
            itemView.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    itemClickListener?.invoke(items[position])
                }
            }
            itemView.setOnLongClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    itemLongClickListener?.invoke(items[position])
                    true
                } else {
                    false
                }
            }
        }
    }

    override fun onBindViewHolder(holder: BaseViewHolder<B, T>, position: Int) {
        holder.bind(items[position], position)
    }

    override fun onBindViewHolder(
        holder: BaseViewHolder<B, T>,
        position: Int,
        payloads: MutableList<Any>
    ) {
        holder.bind(items[position], position)
    }

    override fun getItemCount() = items.size

    abstract fun bind(binding: B, item: T, position: Int?)

    fun setOnItemClickListener(listener: (T) -> Unit) {
        itemClickListener = listener
    }

    fun setOnItemLongClickListener(listener: (T) -> Unit) {
        itemLongClickListener = listener
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateItems(newItems: List<T>, forceUpdate: Boolean) {
        val diffResult = DiffUtil.calculateDiff(DiffCallback(items, newItems))
        items.clear()
        items.addAll(newItems.toList())
        diffResult.dispatchUpdatesTo(this)

        if (forceUpdate) notifyDataSetChanged()
    }

    private inner class DiffCallback(
        private val oldList: List<T>,
        private val newList: List<T>
    ) : DiffUtil.Callback() {
        override fun getOldListSize() = oldList.size
        override fun getNewListSize() = newList.size
        override fun areItemsTheSame(oldPos: Int, newPos: Int) = oldList[oldPos] == newList[newPos]
        override fun areContentsTheSame(oldPos: Int, newPos: Int) = oldList[oldPos] == newList[newPos]
        override fun getChangePayload(oldPos: Int, newPos: Int): Any = Any()
    }
}
