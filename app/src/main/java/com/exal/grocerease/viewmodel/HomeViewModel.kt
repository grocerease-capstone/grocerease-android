package com.exal.grocerease.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.exal.grocerease.helper.Resource
import com.exal.grocerease.model.db.entities.ListEntity
import com.exal.grocerease.model.network.response.GetListResponse
import com.exal.grocerease.model.repository.DataRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(private val dataRepository: DataRepository) : ViewModel() {

    private val _expenses = MutableLiveData<PagingData<ListEntity>>()
    val expenses: LiveData<PagingData<ListEntity>> get() = _expenses

    private val _totalExpenses = MutableLiveData<Int>()
    val totalExpenses: LiveData<Int> get() = _totalExpenses

    private val _totalItems = MutableLiveData<Int>()
    val totalItems: LiveData<Int> get() = _totalItems

    fun setLists(list: List<ListEntity>) {
        _totalItems.value = list.sumOf { item ->
            val totalItems = item.totalItems ?: 0
            totalItems
        }
        _totalExpenses.value = list.sumOf { item ->
            val totalExpenses = item.totalExpenses?.toDoubleOrNull()?.toInt() ?: 0
            totalExpenses
        }
    }

    fun getLists(type: String) {
        viewModelScope.launch {
            dataRepository.getFiveListData(type)
                .cachedIn(viewModelScope)
                .collectLatest { pagingData ->
                    _expenses.postValue(pagingData)
                }
        }
    }
}