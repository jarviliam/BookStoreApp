package com.example.googlebooksapi.UI.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.googlebooksapi.R
import com.example.googlebooksapi.databinding.HomeFragmentBinding
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class HomeFragment : Fragment(R.layout.home_fragment) {

    private val viewModel by viewModels<HomeViewModel>()


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val adapter = BooksAdapter()

        val audioAdapter = AudioBooksAdapter()
        Timber.i("Here")
        val binding = HomeFragmentBinding.bind(view)
        with(binding) {
            Timber.i("Home Fragment Binded")
            this.helloText.text = "Hello, ${viewModel.userName.value}"
            this.homeBooksRV.layoutManager =
                LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            this.homeBooksRV.adapter = adapter

            this.audioBooksRV.layoutManager = LinearLayoutManager(context,LinearLayoutManager.HORIZONTAL,false)
            this.audioBooksRV.adapter = audioAdapter

            viewModel.bookList.observe(viewLifecycleOwner, {
                adapter.updateList(it)
            })
            viewModel.audioBookList.observe(viewLifecycleOwner, {
                audioAdapter.updateListings(it)
            })

        }
    }

}