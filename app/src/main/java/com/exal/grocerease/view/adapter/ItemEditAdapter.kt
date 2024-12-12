package com.exal.grocerease.view.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.exal.grocerease.databinding.ItemEditItemBinding
import com.exal.grocerease.helper.rupiahFormatter
import com.exal.grocerease.model.network.response.ProductItemsItem

class ItemEditAdapter(private val onEdit: (ProductItemsItem) -> Unit): ListAdapter<ProductItemsItem, ItemEditAdapter.ItemViewHolder>(DIFF_CALLBACK){

    private val categoryMapping = mapOf(
        "0" to "Food",
        "1" to "Beauty",
        "2" to "Home Living",
        "3" to "Drink",
        "4" to "Fresh Product",
        "5" to "Health",
        "6" to "Other"
    )

    inner class ItemViewHolder(private val binding: ItemEditItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: ProductItemsItem) {
            val category = item.category
            Log.d("ItemEditAdapter", "Category: $category")
            with(binding) {
                itemName.text = item.name
                itemCategory.text = categoryMapping[category.toString()]
                itemQuantity.text = item.amount.toString()
                itemPrice.text = item.price?.let { rupiahFormatter(it.toDouble().toInt()) }
                editBtn.setOnClickListener {
                    onEdit(item)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val binding = ItemEditItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ItemViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<ProductItemsItem>(){
            override fun areItemsTheSame(oldItem: ProductItemsItem, newItem: ProductItemsItem): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: ProductItemsItem, newItem: ProductItemsItem): Boolean {
                return oldItem == newItem
            }
        }
    }
}