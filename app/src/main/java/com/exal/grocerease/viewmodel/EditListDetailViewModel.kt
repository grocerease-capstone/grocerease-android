package com.exal.grocerease.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.exal.grocerease.model.network.response.ProductsItem
import com.exal.grocerease.model.repository.DataRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class EditListDetailViewModel @Inject constructor(private val dataRepository: DataRepository) :
    ViewModel() {
    private val _productList = MutableLiveData<List<ProductsItem>>()
    val productList: LiveData<List<ProductsItem>> get() = _productList

    private val _totalPrice = MutableLiveData<Int>()
    val totalPrice: LiveData<Int> get() = _totalPrice

    // TODO(Post Edit List Detail to Server)
    fun setInitialProductList(products: List<ProductsItem>) {
        _productList.value = products
        val newTotalPrice = products.sumOf { (it.price ?: 0) * (it.amount ?: 0) }
        _totalPrice.value = newTotalPrice
    }

    fun deleteProduct(item: ProductsItem) {
        val currentList = _productList.value.orEmpty().toMutableList()
        currentList.remove(item)
        _productList.value = currentList

        val newTotalPrice = currentList.sumOf { (it.price ?: 0) * (it.amount ?: 0) }
        _totalPrice.value = newTotalPrice
    }

    fun addProduct(product: ProductsItem) {
        val currentList = _productList.value.orEmpty().toMutableList()
        currentList.add(product)
        _productList.value = currentList
        Log.d("CreatePlanViewModel", "Added product: $product")

        val newTotalPrice = currentList.sumOf { (it.price ?: 0) * (it.amount ?: 0) }
        _totalPrice.value = newTotalPrice
        Log.d("CreatePlanViewModel", "Updated total price: $newTotalPrice")
    }
}