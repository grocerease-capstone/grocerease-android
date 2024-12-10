package com.exal.grocerease.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.exal.grocerease.model.db.entities.ListEntity
import com.exal.grocerease.model.repository.DataRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ExpensesViewModel @Inject constructor(private val dataRepository: DataRepository) :
    ViewModel() {
    private val _expenses = MutableLiveData<PagingData<ListEntity>>()
    val expenses: LiveData<PagingData<ListEntity>> get() = _expenses

    private val _toastEvent = MutableSharedFlow<String>()
    val toastEvent: SharedFlow<String> get() = _toastEvent

    fun getLists(type: String) {
        viewModelScope.launch {
            dataRepository.getListData(type)
                .cachedIn(viewModelScope)
                .collectLatest { pagingData ->
                    _expenses.value = pagingData
                }
        }
    }

    fun filterData(type: String, month: Int, year: Int) {
        viewModelScope.launch {
            dataRepository.getFilterData(type, month, year, _toastEvent)
                .cachedIn(viewModelScope)
                .collectLatest { pagingData ->
                    _expenses.value = pagingData
                }
        }
    }
}