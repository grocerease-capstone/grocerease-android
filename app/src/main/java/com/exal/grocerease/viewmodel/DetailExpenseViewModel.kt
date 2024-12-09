package com.exal.grocerease.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.exal.grocerease.helper.Resource
import com.exal.grocerease.model.network.response.DetailListResponse
import com.exal.grocerease.model.repository.DataRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailExpenseViewModel @Inject constructor(private val dataRepository: DataRepository) :
    ViewModel() {
    private val _productList = MutableLiveData<Resource<DetailListResponse>>()
    val productList: LiveData<Resource<DetailListResponse>> get() = _productList

    fun getExpenseDetail(id: Int) {
        viewModelScope.launch {
            dataRepository.getExpensesDetail(id)
                .collect { resource ->
                    _productList.value = resource
                }
        }
    }
}