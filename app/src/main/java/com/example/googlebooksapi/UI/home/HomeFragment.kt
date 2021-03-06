package com.example.googlebooksapi.UI.home

import android.content.Context
import android.graphics.Typeface
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import android.view.View
import android.widget.ImageView
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.googlebooksapi.CustomLayout
import com.example.googlebooksapi.MainActivity
import com.example.googlebooksapi.R
import com.example.googlebooksapi.aou.ItemObj
import com.example.googlebooksapi.databinding.HomeFragmentBinding
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber
import javax.inject.Inject

@AndroidEntryPoint
class HomeFragment : Fragment(R.layout.home_fragment) {

    private val viewModel by viewModels<HomeViewModel>()

    @Inject
    lateinit var ctx: Context

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val adapter = BooksAdapter{ itemObj: ItemObj, imageView: ImageView ->
            handleAdapterClick(itemObj, imageView)
        }

        val audioAdapter = AudioBooksAdapter()
        Timber.i("Here")
        val binding = HomeFragmentBinding.bind(view)
        with(binding) {
            Timber.i("Home Fragment Binded")
            this.helloText.text = "Hello, ${viewModel.userName.value}"

            this.popularTV.setOnClickListener { l ->
                if (viewModel.bookSearchType.value !== "Popular") {
                    viewModel.bookSearchType.postValue("Popular")
                }
            }
            this.newestTV.setOnClickListener { l ->
                if (viewModel.bookSearchType.value !== "Newest") {
                    viewModel.bookSearchType.postValue("Newest")
                }
            }
            this.bestSellerTV.setOnClickListener { l ->
                if (viewModel.bookSearchType.value !== "Bestsellers") {
                    viewModel.bookSearchType.postValue("Bestsellers")
                }
            }
            viewModel.bookSearchType.observe(viewLifecycleOwner, {
               listOf(this.popularTV,this.newestTV,this.bestSellerTV).map {
                   x -> if(x.text == it)x.typeface = Typeface.DEFAULT_BOLD else x.typeface = Typeface.DEFAULT
               }
                viewModel.bookTypeChanged(it) })


            //Adapters
            this.homeBooksRV.layoutManager =
                LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            this.homeBooksRV.adapter = adapter
            this.homeBooksRV.adapter

            this.audioBooksRV.layoutManager =
                LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            this.audioBooksRV.adapter = audioAdapter
            this.helloText.setOnClickListener {
            }

            viewModel.bookList.observe(viewLifecycleOwner, {
                adapter.updateList(it)
            })
            viewModel.audioBookList.observe(viewLifecycleOwner, {
                audioAdapter.updateListings(it)
            })

        }
    }
    private fun handleAdapterClick(item : ItemObj, img: ImageView){
        Timber.i("Adapter click")
        val extras = FragmentNavigatorExtras(img to "snappedIV")
        this.findNavController().navigate(HomeFragmentDirections.homeToBook())
    }

}