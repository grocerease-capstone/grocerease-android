package com.exal.grocerease.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.exal.grocerease.databinding.ItemSharedListBinding
import com.exal.grocerease.helper.DateFormatter
import com.exal.grocerease.model.network.response.AllDetailListItem

class SharedListAdapter(private val onItemClick: (Int, String, String, String) -> Unit): ListAdapter<AllDetailListItem, SharedListAdapter.ItemViewHolder>(DIFF_CALLBACK){

    inner class ItemViewHolder(private val binding: ItemSharedListBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: AllDetailListItem) {
            binding.titleTv.text = item.title
            "${item.totalItems} Item".also { binding.totalTv.text = it }
            binding.dayTv.text = DateFormatter.localizeDay(item.boughtAt ?: "")
            binding.dateTv.text = DateFormatter.localizeMonth(item.boughtAt ?: "")
            binding.yearTv.text = DateFormatter.localizeYear(item.boughtAt ?: "")
            binding.ownerTv.text = item.sender.toString()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val binding = ItemSharedListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ItemViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val item = getItem(position)
        if (item != null) {
            holder.bind(item)
        }
        holder.itemView.setOnClickListener {
            if (item != null) {
                item.id?.let { it1 -> onItemClick(it1.toInt(), item.title ?: "", item.boughtAt ?: "", item.type ?: "") }
            }
        }
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<AllDetailListItem>(){
            override fun areItemsTheSame(oldItem: AllDetailListItem, newItem: AllDetailListItem): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: AllDetailListItem, newItem: AllDetailListItem): Boolean {
                return oldItem == newItem
            }
        }
    }
}
