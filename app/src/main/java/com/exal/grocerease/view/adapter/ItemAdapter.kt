package com.exal.grocerease.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.exal.grocerease.R
import com.exal.grocerease.model.network.response.DataItem
import com.exal.grocerease.model.network.response.ProductsItem
import com.exal.grocerease.databinding.ItemExpensesItemBinding
import com.exal.grocerease.databinding.ItemRowExpenseBinding
import com.exal.grocerease.helper.DateFormatter
import com.exal.grocerease.helper.rupiahFormatter

class ItemAdapter(private val onDelete: (ProductsItem) -> Unit): ListAdapter<ProductsItem, ItemAdapter.ItemViewHolder>(DIFF_CALLBACK){

    private val categoryMapping = mapOf(
        "home living" to "Home & Living",
        "minuman" to "Drink",
        "product-segar" to "Fresh Product",
        "kecantikan" to "Beauty",
        "kesehatan" to "Health",
        "makanan" to "Food",
        "lainnya" to "Other"
    )

    inner class ItemViewHolder(private val binding: ItemExpensesItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: ProductsItem) {
            with(binding) {
                itemName.text = item.name
                itemCategory.text = categoryMapping[item.detail?.category]
                itemQuantity.text = item.amount.toString()
                itemPrice.text = item.price?.let { rupiahFormatter(it) }

                deleteButton.setOnClickListener {
                    onDelete(item)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val binding = ItemExpensesItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ItemViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<ProductsItem>(){
            override fun areItemsTheSame(oldItem: ProductsItem, newItem: ProductsItem): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: ProductsItem, newItem: ProductsItem): Boolean {
                return oldItem == newItem
            }
        }
    }
}