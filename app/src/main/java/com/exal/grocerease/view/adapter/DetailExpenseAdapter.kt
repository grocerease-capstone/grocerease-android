package com.exal.grocerease.view.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.exal.grocerease.databinding.ItemDetailListBinding
import com.exal.grocerease.helper.rupiahFormatter
import com.exal.grocerease.model.network.response.DetailItemsItem

class DetailExpenseAdapter: ListAdapter<DetailItemsItem, DetailExpenseAdapter.ItemViewHolder>(DIFF_CALLBACK){

    private val categoryMapping = mapOf(
        "0" to "Food",
        "1" to "Beauty",
        "2" to "Home Living",
        "3" to "Drink",
        "4" to "Fresh Product",
        "5" to "Health",
        "6" to "Other"
    )

    inner class ItemViewHolder(private val binding: ItemDetailListBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: DetailItemsItem) {
            with(binding) {
                itemName.text = item.name
                itemCategory.text = categoryMapping[item.category]
                itemQuantity.text = item.amount.toString()
                if (item.price == "0") {
                    priceTxt.visibility = View.GONE
                    itemPrice.visibility = View.GONE
                } else {
                    itemPrice.text = item.price?.let { rupiahFormatter(it.toInt()) }
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val binding = ItemDetailListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ItemViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<DetailItemsItem>(){
            override fun areItemsTheSame(oldItem: DetailItemsItem, newItem: DetailItemsItem): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: DetailItemsItem, newItem: DetailItemsItem): Boolean {
                return oldItem == newItem
            }
        }
    }
}