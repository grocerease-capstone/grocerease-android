package com.exal.grocerease.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.exal.grocerease.helper.Resource
import com.exal.grocerease.model.repository.DataRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(private val dataRepository: DataRepository) : ViewModel() {
    private val _logoutState = MutableLiveData<Resource<Boolean>>()
    val logoutState: LiveData<Resource<Boolean>> get() = _logoutState

    fun logout() {
        viewModelScope.launch {
            dataRepository.logout().collect { resource ->
                _logoutState.postValue(resource)
            }
        }
    }
}