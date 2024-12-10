package com.exal.grocerease.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.exal.grocerease.helper.Resource
import com.exal.grocerease.model.network.response.GetAccountResponse
import com.exal.grocerease.model.network.response.UpdateListResponse
import com.exal.grocerease.model.repository.DataRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import okhttp3.RequestBody
import javax.inject.Inject

@HiltViewModel
class AccountSettingsViewModel @Inject constructor(private val dataRepository: DataRepository) :
    ViewModel() {
    private val _accountData = MutableLiveData<Resource<GetAccountResponse>>()
    val accountData: LiveData<Resource<GetAccountResponse>> get() = _accountData

    fun getAccount() {
        viewModelScope.launch {
            dataRepository.getAccount().collect { resource ->
                _accountData.postValue(resource)
            }
        }
    }

    fun updateData(
        profileImage: MultipartBody.Part?,
        username: RequestBody?,
        password: RequestBody?,
        passwordNew: RequestBody?
    ): Flow<Resource<UpdateListResponse>> = dataRepository.updateAccount(
        profileImage,
        username,
        password,
        passwordNew
    )
}