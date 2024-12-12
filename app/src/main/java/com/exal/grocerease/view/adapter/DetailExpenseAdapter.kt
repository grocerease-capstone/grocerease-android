package com.exal.grocerease.view.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.exal.grocerease.databinding.ItemDetailListBinding
import com.exal.grocerease.helper.rupiahFormatter
import com.exal.grocerease.model.network.response.ProductItemsItem

class DetailExpenseAdapter: ListAdapter<ProductItemsItem, DetailExpenseAdapter.ItemViewHolder>(DIFF_CALLBACK){

    private val categoryMapping = mapOf(
        "0" to "Makanan",
        "1" to "Kecantikan",
        "2" to "Home Living",
        "3" to "Minuman",
        "4" to "Produk Segar",
        "5" to "Kesehatan",
        "6" to "Lainnya"
    )

    inner class ItemViewHolder(private val binding: ItemDetailListBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: ProductItemsItem) {
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