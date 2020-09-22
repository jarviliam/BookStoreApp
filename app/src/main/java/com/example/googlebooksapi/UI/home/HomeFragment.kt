package com.example.googlebooksapi.UI.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import android.view.View
import androidx.lifecycle.Observer
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

        Timber.i("Here")
        val binding = HomeFragmentBinding.bind(view)
        with(binding) {
            Timber.i("Home Fragment Binded")
            this.helloText.text = "Hello, ${viewModel.userName.value}"
            this.homeBooksRV.layoutManager =
                LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            this.homeBooksRV.adapter = adapter

            viewModel.bookList.observe(viewLifecycleOwner, {
                adapter.updateList(it)
            })

        }
    }

}