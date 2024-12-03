package com.exal.grocerease.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.exal.grocerease.model.network.response.ProductsItem
import com.exal.grocerease.model.network.response.ScanImageResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class EditListViewModel @Inject constructor() : ViewModel() {

    private val _scanData = MutableLiveData<ScanImageResponse?>()
    val scanData: LiveData<ScanImageResponse?> get() = _scanData

    fun setScanData(scanImageResponse: ScanImageResponse?) {
        if (_scanData.value == null) {
            scanImageResponse?.let { response ->
                val productsWithId =
                    response.products?.filterNotNull()?.mapIndexed { index, product ->
                        product.copy(id = index + 1)
                    }
                _scanData.value = response.copy(products = productsWithId)
            }
        }
    }

    fun updateItem(updatedItem: ProductsItem) {
        _scanData.value?.let { currentData ->
            val updatedProducts = currentData.products?.map { product ->
                if (product?.id == updatedItem.id) {
                    updatedItem
                } else {
                    product
                }
            }
            _scanData.value = currentData.copy(products = updatedProducts)
        }
    }
}
