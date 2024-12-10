package com.exal.grocerease.viewmodel

import androidx.lifecycle.ViewModel
import com.exal.grocerease.model.repository.DataRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ShareListViewModel @Inject constructor(private val dataRepository: DataRepository) :
    ViewModel() {

}