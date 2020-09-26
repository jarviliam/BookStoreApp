package com.example.googlebooksapi.UI.home

import android.content.Context
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.example.googlebooksapi.R
import com.example.googlebooksapi.aou.ApiService
import com.example.googlebooksapi.aou.ItemObj
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import timber.log.Timber

class HomeViewModel @ViewModelInject constructor(
    @androidx.hilt.Assisted private val savedStateHandle: SavedStateHandle,
    private val apiService: ApiService,
    private val context: Context
) :
    ViewModel() {

    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    //UserName For Main Page
    val userName: LiveData<String> =
        savedStateHandle.getLiveData<String>("username", "Liam")

    val bookSearchType: MutableLiveData<String> =
        savedStateHandle.getLiveData("bookType", "Popular")
    val bookList: MutableLiveData<List<ItemObj>> = savedStateHandle.getLiveData("bookList")
    val audioBookList: MutableLiveData<List<ItemObj>> = savedStateHandle.getLiveData("audioBooks")

    init {
        Timber.tag("Home View Model")
        scope.launch {
            val resp = apiService.searchBooks("Dazai", context.getString(R.string.api_key))
            Timber.i("Send Api Request")
            Timber.i(resp.message())
            if (resp.isSuccessful) {
                bookList.postValue(resp.body()?.items)
                Timber.i("Should have updated LiveData")
            }
            val resp2 = apiService.searchBooks("Natsume", context.getString(R.string.api_key))
            if (resp2.isSuccessful) {
                Timber.i("Response Success")
                audioBookList.postValue(resp2.body()?.items)
            }
        }

    }

    fun bookTypeChanged(type: String) {
        when (type) {
            "Popular" -> scope.launch { fetchItemsFromRemote("Dazai") }
            "Bestsellers" -> scope.launch { fetchItemsFromRemote("Mahjong") }
            "Newest" -> scope.launch { fetchItemsFromRemote("Near to the") }
        }
    }

    private suspend fun fetchItemsFromRemote(name: String) {
        val response = apiService.searchBooks(name, context.getString(R.string.api_key))
        if (response.isSuccessful) {
            bookList.postValue(response.body()?.items)
        }
    }


}