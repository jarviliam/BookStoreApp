package com.example.googlebooksapi.UI.home

import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import androidx.fragment.app.viewModels
import android.view.View
import android.widget.ImageView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.googlebooksapi.R
import com.example.googlebooksapi.UI.base.BaseFragment
import com.example.googlebooksapi.UI.home.adapters.AudioBooksAdapter
import com.example.googlebooksapi.UI.home.adapters.BooksAdapter
import com.example.googlebooksapi.UI.home.adapters.SingleLayoutManager
import com.example.googlebooksapi.aou.ItemObj
import com.example.googlebooksapi.aou.Temp
import com.example.googlebooksapi.databinding.HomeFragmentBinding
import com.example.googlebooksapi.extensions.viewBinding
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class HomeFragment : BaseFragment(R.layout.home_fragment) {

    private val viewModel by viewModels<HomeViewModel>()
    private val binding by viewBinding(HomeFragmentBinding::bind)

    private var adapter: BooksAdapter? = null
    private var audioAdapter: AudioBooksAdapter? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        makeAdapters()
        setupUI()
        setupObs()

        binding.taby
    }

    private fun setupUI() {
        with(binding) {
            helloText.text = "Hello, ${viewModel.userName.value}"

            popularTV.setOnClickListener { l ->
                if (viewModel.bookSearchType.value !== "Popular") {
                    viewModel.bookSearchType.postValue("Popular")
                }
            }
            newestTV.setOnClickListener { l ->
                if (viewModel.bookSearchType.value !== "Newest") {
                    viewModel.bookSearchType.postValue("Newest")
                }
            }
            bestSellerTV.setOnClickListener { l ->
                if (viewModel.bookSearchType.value !== "Bestsellers") {
                    viewModel.bookSearchType.postValue("Bestsellers")
                }
            }

        }
    }

    private fun makeAdapters() {
        adapter = BooksAdapter { itemObj: ItemObj, imageView: ImageView ->
            handleAdapterClick(itemObj, imageView)
        }
        audioAdapter = AudioBooksAdapter()
        val li = listOf(Temp(Color.RED), Temp(Color.BLUE), Temp(Color.YELLOW))

        adapter?.updateList(li)
        binding.homeBooksRV.apply {
            layoutManager = SingleLayoutManager()
            setHasFixedSize(true)
            //LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            adapter = this@HomeFragment.adapter
            /*addOnScrollListener(object: RecyclerView.OnScrollListener() {
                override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                    super.onScrollStateChanged(recyclerView, newState)
                    val manager = recyclerView.layoutManager as SingleLayoutManager
                    if(RecyclerView.SCROLL_STATE_IDLE == newState){
                        Timber.i("HERE")
                        recyclerView.smoothScrollBy(manager.getOffsetFromCenterView(),0)
                    }
                }

            })*/
        }
        binding.audioBooksRV.apply {
            layoutManager =
                LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            adapter = audioAdapter
        }
    }

    private fun setupObs() {

        viewModel.bookSearchType.observe(viewLifecycleOwner, {
            listOf(binding.popularTV, binding.newestTV, binding.bestSellerTV).map { x ->
                if (x.text == it) x.typeface = Typeface.DEFAULT_BOLD else x.typeface =
                    Typeface.DEFAULT
            }
        })
        viewModel.bookList.observe(viewLifecycleOwner, {
        })
        viewModel.audioBookList.observe(viewLifecycleOwner, {
            audioAdapter?.updateListings(it)
        })

    }

    private fun handleAdapterClick(item: ItemObj, img: ImageView) {
    }

    override fun onDestroyView() {
        super.onDestroyView()
        adapter = null
        audioAdapter = null
    }
}