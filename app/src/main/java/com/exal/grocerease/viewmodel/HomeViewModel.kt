package com.exal.grocerease.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.exal.grocerease.helper.Resource
import com.exal.grocerease.model.network.response.GetListResponse
import com.exal.grocerease.model.repository.DataRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(private val dataRepository: DataRepository) : ViewModel() {
    private val _expenses = MutableLiveData<Resource<GetListResponse>>()
    val expenses: LiveData<Resource<GetListResponse>> get() = _expenses

    fun getExpenseList() {
        viewModelScope.launch {
            dataRepository.getExpenseList()
                .collect { resource ->
                    _expenses.postValue(resource)
                }
        }
    }
}