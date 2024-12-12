package com.exal.grocerease.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.exal.grocerease.databinding.ItemPlanListBinding
import com.exal.grocerease.helper.DateFormatter
import com.exal.grocerease.model.db.entities.ListEntity

class PlanAdapter(private val onItemClick: (Int, String, String) -> Unit): PagingDataAdapter<ListEntity, PlanAdapter.ItemViewHolder>(DIFF_CALLBACK){

    inner class ItemViewHolder(private val binding: ItemPlanListBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: ListEntity) {
            binding.titleTv.text = item.title
            "${item.totalItems} Item".also { binding.totalTv.text = it }
            binding.dayTv.text = DateFormatter.localizeDay(item.boughtAt ?: "")
            binding.dateTv.text = DateFormatter.localizeMonth(item.boughtAt ?: "")
            binding.yearTv.text = DateFormatter.localizeYear(item.boughtAt ?: "")
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val binding = ItemPlanListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ItemViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val item = getItem(position)
        if (item != null) {
            holder.bind(item)
        }
        holder.itemView.setOnClickListener {
            if (item != null) {
                onItemClick(item.id.toInt(), item.title ?: "", item.boughtAt ?: "")
            }
        }
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<ListEntity>(){
            override fun areItemsTheSame(oldItem: ListEntity, newItem: ListEntity): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: ListEntity, newItem: ListEntity): Boolean {
                return oldItem == newItem
            }
        }
    }
}
