package com.example.googlebooksapi.UI.audiobookDetail

import android.content.Context
import androidx.hilt.lifecycle.ViewModelInject
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

class AudioBookDetailViewModel @ViewModelInject constructor(
    @androidx.hilt.Assisted private val savedStateHandle: SavedStateHandle,
    apiService: ApiService,
    context: Context
) : ViewModel() {

    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    val bookList: MutableLiveData<List<ItemObj>> = savedStateHandle.getLiveData("detailedBookList")

    init {
        scope.launch {
            val resp = apiService.searchBooks("Nietzche", context.getString(R.string.api_key))
            if (resp.isSuccessful) {
                bookList.postValue(resp.body()!!.items)
            }
        }
    }


}