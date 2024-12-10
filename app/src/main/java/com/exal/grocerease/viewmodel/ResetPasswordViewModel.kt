package com.exal.grocerease.viewmodel

import androidx.lifecycle.ViewModel
import com.exal.grocerease.helper.Resource
import com.exal.grocerease.model.network.response.UpdateListResponse
import com.exal.grocerease.model.repository.DataRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import okhttp3.MultipartBody
import okhttp3.RequestBody
import javax.inject.Inject

@HiltViewModel
class ResetPasswordViewModel @Inject constructor(private val dataRepository: DataRepository) :
    ViewModel() {

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