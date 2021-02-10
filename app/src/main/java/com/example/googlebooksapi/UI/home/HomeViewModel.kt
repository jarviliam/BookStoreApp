package com.example.googlebooksapi.UI.home

import android.content.Context
import androidx.lifecycle.LiveData
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
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val apiService: ApiService,
) :
    ViewModel() {
    private val _userName: MutableLiveData<String> =
        savedStateHandle.getLiveData<String>("username", "Liam")
    val userName: LiveData<String>
        get() = _userName


    val bookSearchType: MutableLiveData<String> =
        savedStateHandle.getLiveData("bookType", "Popular")
    val bookList: MutableLiveData<List<ItemObj>> = savedStateHandle.getLiveData("bookList")
    val audioBookList: MutableLiveData<List<ItemObj>> = savedStateHandle.getLiveData("audioBooks")

    fun searchBooks(query: String) {

    }
}