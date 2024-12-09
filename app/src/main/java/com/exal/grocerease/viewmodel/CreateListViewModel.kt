package com.exal.grocerease.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.exal.grocerease.helper.Resource
import com.exal.grocerease.model.repository.DataRepository
import com.exal.grocerease.model.network.response.PostListResponse
import com.exal.grocerease.model.network.response.ProductsItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import okhttp3.MultipartBody
import okhttp3.RequestBody
import javax.inject.Inject

@HiltViewModel
class CreateListViewModel @Inject constructor(private val dataRepository: DataRepository) : ViewModel() {
    private val _productList = MutableLiveData<List<ProductsItem>>()
    val productList: LiveData<List<ProductsItem>> get() = _productList

    private val _totalPrice = MutableLiveData<Int>()
    val totalPrice: LiveData<Int> get() = _totalPrice

    init{
        _productList.value = emptyList()
        _totalPrice.value = 0
    }

    fun postData(
        title: RequestBody,
        receiptImage: MultipartBody.Part?,
        thumbnailImage: MultipartBody.Part?,
        productItems: RequestBody,
        type: RequestBody,
        totalExpenses: RequestBody,
        totalItems: RequestBody,
        boughtAt: RequestBody,
    ): Flow<Resource<PostListResponse>> = dataRepository.postData(
        title,
        receiptImage,
        thumbnailImage,
        productItems,
        type,
        totalExpenses,
        totalItems,
        boughtAt
    )

    private val _imageUri = MutableLiveData<String>()
    val imageUri: LiveData<String> get() = _imageUri

    fun setProductList(products: List<ProductsItem>, price: Int) {
        _productList.value = products
        _totalPrice.value = price
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

        val newTotalPrice = currentList.sumOf { (it.price ?: 0) * (it.amount ?: 0) }
        _totalPrice.value = newTotalPrice
    }

    fun setImageUri(uri: String) {
        _imageUri.value = uri
    }
}
