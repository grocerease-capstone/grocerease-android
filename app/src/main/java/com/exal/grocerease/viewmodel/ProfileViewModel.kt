package com.exal.grocerease.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.exal.grocerease.helper.Resource
import com.exal.grocerease.model.network.response.GetProfileResponse
import com.exal.grocerease.model.network.response.ListsByWeekItem
import com.exal.grocerease.model.repository.DataRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(private val dataRepository: DataRepository) : ViewModel() {
    private val _logoutState = MutableLiveData<Resource<Boolean>>()
    val logoutState: LiveData<Resource<Boolean>> get() = _logoutState

    private val _chartData = MutableLiveData<List<Double>>()
    val chartData: LiveData<List<Double>> get() = _chartData

    private val _accountData = MutableLiveData<Resource<GetProfileResponse>>()
    val accountData: LiveData<Resource<GetProfileResponse>> get() = _accountData

    fun setChartData(data: List<ListsByWeekItem?>?) {
        val chartDataList = data?.mapNotNull { it?.totalExpenses?.toDouble() } ?: emptyList()
        _chartData.postValue(chartDataList)
    }

    fun logout() {
        viewModelScope.launch {
            dataRepository.logout().collect { resource ->
                _logoutState.postValue(resource)
            }
        }
    }

    fun clearDatabase() {
        viewModelScope.launch {
            dataRepository.deleteAllDatabaseData().collect { resource ->
                _logoutState.postValue(resource)
            }
        }
    }

    fun getAccount() {
        viewModelScope.launch {
            dataRepository.getAccount().collect { resource ->
                _accountData.postValue(resource)
            }
        }
    }
}