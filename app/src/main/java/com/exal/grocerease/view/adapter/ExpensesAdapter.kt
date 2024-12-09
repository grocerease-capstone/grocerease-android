package com.exal.grocerease.view.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.exal.grocerease.R
import com.exal.grocerease.databinding.ItemRowExpenseBinding
import com.exal.grocerease.helper.DateFormatter
import com.exal.grocerease.helper.rupiahFormatter
import com.exal.grocerease.model.db.entities.ListEntity
import com.exal.grocerease.model.network.response.ListsItem

class ExpensesAdapter(private val onItemClick: (Int, String, String) -> Unit): PagingDataAdapter<ListEntity, ExpensesAdapter.ItemViewHolder>(DIFF_CALLBACK){

    inner class ItemViewHolder(private val binding: ItemRowExpenseBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: ListEntity) {
            binding.itemImage.setImageResource(R.drawable.placeholder)
            val totalExpensesInt = item.totalExpenses?.toDoubleOrNull()?.toInt()
            binding.itemPrice.text = totalExpensesInt?.let { rupiahFormatter(it) } ?: "0"
            "${item.totalItems} Items".also { binding.itemTotal.text = it }
            binding.itemDate.text = DateFormatter.localizeDate(item.boughtAt ?: "")
            Glide.with(itemView.context)
                .load(item.image)
                .apply(
                    RequestOptions.placeholderOf(R.drawable.placeholder)
                        .error(R.drawable.ic_close)
                )
                .into(binding.itemImage)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val binding = ItemRowExpenseBinding.inflate(LayoutInflater.from(parent.context), parent, false)
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
