package com.exal.grocerease.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.exal.grocerease.helper.Resource
import com.exal.grocerease.model.network.response.GetSharedListResponse
import com.exal.grocerease.model.network.response.RequestDetailItem
import com.exal.grocerease.model.network.response.UpdateListResponse
import com.exal.grocerease.model.repository.DataRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NotificationViewModel @Inject constructor(private val dataRepository: DataRepository): ViewModel() {
    private val _notificationList = MutableLiveData<Resource<GetSharedListResponse>>()
    val notificationList: LiveData<Resource<GetSharedListResponse>> = _notificationList

    private val _acceptNotification = MutableLiveData<Resource<UpdateListResponse>>()
    val acceptNotification: LiveData<Resource<UpdateListResponse>> = _acceptNotification

    fun getNotificationList() {
        viewModelScope.launch {
            dataRepository.getSharedList().collect { resource ->
                _notificationList.postValue(resource)
            }
        }
    }

    fun acceptNotification(id: Int) {
        viewModelScope.launch {
            dataRepository.acceptNotification(id).collect { resource ->
                _acceptNotification.postValue(resource)
            }
        }
    }

    fun declineNotification(id: Int) {
        viewModelScope.launch {
            dataRepository.declineNotification(id).collect { resource ->
                _acceptNotification.postValue(resource)
            }
        }
    }
}