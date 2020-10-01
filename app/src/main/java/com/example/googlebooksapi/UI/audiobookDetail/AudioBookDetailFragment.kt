package com.example.googlebooksapi.UI.audiobookDetail

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SnapHelper
import com.example.googlebooksapi.CustomLayout
import com.example.googlebooksapi.MainActivity
import com.example.googlebooksapi.R
import com.example.googlebooksapi.databinding.AudioBookDetailFragmentBinding
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber
import javax.inject.Inject

@AndroidEntryPoint
class AudioBookDetailFragment : Fragment(R.layout.audio_book_detail_fragment) {

    private val viewModel by viewModels<AudioBookDetailViewModel>()

    @Inject
    lateinit var ctx: Context

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        super.onViewCreated(view, savedInstanceState)
        Timber.tag("Audio Book Detail")


        val x = requireActivity() as MainActivity
        x.hideNavigation()
        Timber.i("On View Created")

        val binding = AudioBookDetailFragmentBinding.bind(view)

        val adap = AuthorDetailsAdapter()
        val snap = PagerSnapHelper()
        with(binding) {
            snap.attachToRecyclerView(this.recyclerView)
            this.recyclerView.apply {
                layoutManager = CustomLayout(ctx)
                //layoutManager = LinearLayoutManager(ctx, LinearLayoutManager.HORIZONTAL, false)
                snap.findSnapView(layoutManager)
                adapter = adap
            }

            viewModel.bookList.observe(viewLifecycleOwner, {
                adap.updateListing(it)
            })
            this.backButton.setOnClickListener {
                view.findNavController().popBackStack()
            }
        }
    }
}