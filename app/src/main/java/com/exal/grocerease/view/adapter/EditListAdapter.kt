package com.exal.grocerease.view.adapter

import android.R
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.exal.grocerease.databinding.ItemEditListBinding
import com.exal.grocerease.helper.rupiahFormatter
import com.exal.grocerease.model.network.response.ProductsItem

class EditListAdapter(
    private val onItemUpdated: (ProductsItem) -> Unit
) : ListAdapter<ProductsItem, EditListAdapter.ItemViewHolder>(DIFF_CALLBACK) {

    private val categoryMapping = mapOf(
        0 to "Food",
        1 to "Beauty",
        2 to "Home Living",
        3 to "Drink",
        4 to "Fresh Product",
        5 to "Health",
        6 to "Other"
    )

    private val reverseCategoryMapping = categoryMapping.entries.associate { (key, value) -> value to key }

    inner class ItemViewHolder(private val binding: ItemEditListBinding) : RecyclerView.ViewHolder(binding.root) {

        private var isUpdating = false

        fun bind(item: ProductsItem) {
            val categories = categoryMapping.values.toList()
            val adapter = ArrayAdapter(
                binding.textFieldCategory.context,
                R.layout.simple_list_item_1,
                categories
            )
            (binding.textFieldCategory.editText as? AutoCompleteTextView)?.setAdapter(adapter)

            if (binding.textFieldName.editText?.text.toString() != item.name) {
                isUpdating = true
                binding.textFieldName.editText?.setText(item.name)
                isUpdating = false
            }

            val displayCategory = categoryMapping[item.detail?.categoryIndex]
            if (binding.textFieldCategory.editText?.text.toString() != displayCategory) {
                isUpdating = true
                (binding.textFieldCategory.editText as? AutoCompleteTextView)?.setText(displayCategory, false)
                isUpdating = false
            }

            if (binding.textFieldPrice.editText?.text.toString() != item.price.toString()) {
                isUpdating = true
                binding.textFieldPrice.editText?.setText(item.price.toString())
                isUpdating = false
            }

            if (binding.textFieldQuantity.editText?.text.toString() != item.amount.toString()) {
                isUpdating = true
                binding.textFieldQuantity.editText?.setText(item.amount.toString())
                isUpdating = false
            }

            updateTotal(item.price ?: 0, item.amount ?: 0)

            binding.textFieldName.editText?.onFocusChangeListener = View.OnFocusChangeListener { _, hasFocus ->
                if (!hasFocus && !isUpdating) {
                    val updatedName = binding.textFieldName.editText?.text.toString()
                    if (updatedName != item.name) {
                        onItemUpdated(item.copy(name = updatedName))
                    }
                }
            }

            (binding.textFieldCategory.editText as? AutoCompleteTextView)?.setOnItemClickListener { _, _, position, _ ->
                val selectedCategory = categories[position]
                val originalCategory = reverseCategoryMapping[selectedCategory]
                if (originalCategory != item.detail?.categoryIndex) {
                    onItemUpdated(item.copy(detail = item.detail?.copy(categoryIndex = originalCategory)))
                }
            }

            binding.textFieldQuantity.editText?.onFocusChangeListener = View.OnFocusChangeListener { _, hasFocus ->
                if (!hasFocus && !isUpdating) {
                    val updatedQuantity = binding.textFieldQuantity.editText?.text.toString().toIntOrNull() ?: item.amount
                    val updatedPrice = binding.textFieldPrice.editText?.text.toString().toIntOrNull() ?: item.price
                    if (updatedQuantity != item.amount) {
                        updateTotal(updatedPrice!!, updatedQuantity!!)
                        onItemUpdated(item.copy(amount = updatedQuantity))
                    }
                }
            }

            binding.textFieldPrice.editText?.onFocusChangeListener = View.OnFocusChangeListener { _, hasFocus ->
                if (!hasFocus && !isUpdating) {
                    val updatedPrice = binding.textFieldPrice.editText?.text.toString().toIntOrNull() ?: item.price
                    val updatedQuantity = binding.textFieldQuantity.editText?.text.toString().toIntOrNull() ?: item.amount
                    if (updatedPrice != item.price) {
                        updateTotal(updatedPrice!!, updatedQuantity!!)
                        onItemUpdated(item.copy(price = updatedPrice))
                    }
                }
            }
        }

        private fun updateTotal(price: Int, amount: Int) {
            val total = price * amount
            binding.tvTotal.text = rupiahFormatter(total)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val binding = ItemEditListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ItemViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<ProductsItem>() {
            override fun areItemsTheSame(oldItem: ProductsItem, newItem: ProductsItem): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: ProductsItem, newItem: ProductsItem): Boolean {
                return oldItem == newItem
            }
        }
    }
}
