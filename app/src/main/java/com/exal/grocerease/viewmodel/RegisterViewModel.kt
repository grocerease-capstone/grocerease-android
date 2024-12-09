package com.exal.grocerease.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.exal.grocerease.helper.Resource
import com.exal.grocerease.model.repository.DataRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import okhttp3.RequestBody
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(private val dataRepository: DataRepository): ViewModel() {
    private val _registerState = MutableLiveData<Resource<Boolean>>()
    val registerState: LiveData<Resource<Boolean>> get() = _registerState

    fun register(name: RequestBody, email: RequestBody, password: RequestBody, passwordRepeat: RequestBody, profileImage: MultipartBody.Part) {
        viewModelScope.launch {
            dataRepository.register(name, email, password, passwordRepeat, profileImage).collect { resource ->
                _registerState.postValue(resource)
            }
        }
    }
}