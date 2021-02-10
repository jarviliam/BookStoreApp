package com.example.googlebooksapi.UI.audiobookDetail

import android.content.Context
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.example.googlebooksapi.R
import com.example.googlebooksapi.aou.ApiService
import com.example.googlebooksapi.aou.ItemObj
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AudioBookDetailViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    apiService: ApiService
) : ViewModel() {

    val bookList: MutableLiveData<List<ItemObj>> = savedStateHandle.getLiveData("detailedBookList")

}