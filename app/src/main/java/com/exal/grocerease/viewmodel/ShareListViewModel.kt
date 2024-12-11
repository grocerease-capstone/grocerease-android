package com.exal.grocerease.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.exal.grocerease.helper.Resource
import com.exal.grocerease.model.network.response.GetAllSharedListResponse
import com.exal.grocerease.model.network.response.PostListResponse
import com.exal.grocerease.model.repository.DataRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ShareListViewModel @Inject constructor(private val dataRepository: DataRepository) :
    ViewModel() {
    private val _shareList = MutableLiveData<Resource<GetAllSharedListResponse>>()
    val shareList: LiveData<Resource<GetAllSharedListResponse>> = _shareList

    fun getAllSharedList(){
        viewModelScope.launch {
            dataRepository.getAllSharedList().collect { resource ->
                _shareList.postValue(resource)
            }
        }
    }
}