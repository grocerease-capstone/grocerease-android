package com.exal.grocerease.view.fragment

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import androidx.fragment.app.DialogFragment
import com.exal.grocerease.databinding.AddManualDialogFragmentBinding
import com.exal.grocerease.helper.rupiahFormatter
import com.exal.grocerease.model.network.response.ProductItemsItem
import kotlin.text.toIntOrNull

class EditDialogFragment : DialogFragment() {

    private var _binding: AddManualDialogFragmentBinding? = null
    private val binding get() = _binding!!

    private val categoryMapping = mapOf(
        "0" to "Makanan",
        "1" to "Kecantikan",
        "2" to "Home Living",
        "3" to "Minuman",
        "4" to "Produk Segar",
        "5" to "Kesehatan",
        "6" to "Lainnya"
    )

    private val reverseCategoryMapping = categoryMapping.entries.associate { (key, value) -> value to key }

    private var detailItemsItem: ProductItemsItem? = null
    private var onUpdateItemListener: ((ProductItemsItem) -> Unit)? = null

    override fun onResume() {
        super.onResume()
        val window = dialog?.window
        val params = window?.attributes
        params?.width = WindowManager.LayoutParams.MATCH_PARENT
        params?.height = WindowManager.LayoutParams.WRAP_CONTENT
        window?.attributes = params
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = AddManualDialogFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val categories = categoryMapping.values.toList()
        val adapter = ArrayAdapter(
            binding.textFieldCategory.context,
            android.R.layout.simple_list_item_1,
            categories
        )
        (binding.textFieldCategory.editText as? AutoCompleteTextView)?.setAdapter(adapter)

        detailItemsItem?.let { item ->
            binding.textFieldName.editText?.setText(item.name)
            binding.textFieldPrice.editText?.setText(item.price)
            binding.textFieldQuantity.editText?.setText(item.amount.toString())
            (binding.textFieldCategory.editText as? AutoCompleteTextView)?.setText(
                categoryMapping[item.category], false
            )
            updateTotalPrice()
        }

        textListenerAndWatcher()

        binding.addButton.setOnClickListener {
            val productName = binding.textFieldName.editText?.text.toString()
            val productPrice = binding.textFieldPrice.editText?.text.toString()
            val productAmount = binding.textFieldQuantity.editText?.text.toString().toIntOrNull() ?: 0
            val selectedCategory = (binding.textFieldCategory.editText as? AutoCompleteTextView)?.text.toString()
            val categoryKey = reverseCategoryMapping[selectedCategory]

            detailItemsItem?.let { item ->
                val updatedItem = item.copy(
                    name = productName,
                    price = productPrice,
                    amount = productAmount,
                    category = categoryKey,
                    totalPrice = productPrice.toDouble().toInt().times(productAmount).toString()
                )
                onUpdateItemListener?.invoke(updatedItem)
            }

            dismiss()
        }

        binding.cancelButton.setOnClickListener {
            dismiss()
        }
    }

    private fun textListenerAndWatcher(){
        binding.textFieldPrice.editText?.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                updateTotalPrice()
            }

            override fun afterTextChanged(s: Editable?) {}
        })

        binding.textFieldQuantity.editText?.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                updateTotalPrice()
            }

            override fun afterTextChanged(s: Editable?) {}
        })
    }

    private fun updateTotalPrice() {
        val price = binding.textFieldPrice.editText?.text.toString().toIntOrNull() ?: 0
        val quantity = binding.textFieldQuantity.editText?.text.toString().toIntOrNull() ?: 0
        val totalPrice = price * quantity
        binding.tvTotal.text = rupiahFormatter(totalPrice)
    }

    fun setDetailItem(item: ProductItemsItem) {
        detailItemsItem = item
    }

    fun setOnUpdateItemListener(listener: (ProductItemsItem) -> Unit) {
        onUpdateItemListener = listener
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}