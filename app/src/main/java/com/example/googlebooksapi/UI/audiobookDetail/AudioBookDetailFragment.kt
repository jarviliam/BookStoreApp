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
import com.example.googlebooksapi.*
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
        //x.hideNavigation()
        Timber.i("On View Created")

        val binding = AudioBookDetailFragmentBinding.bind(view)

        val list = object : OnSnapPositionChangeListener {
            override fun onSnapPositionChange(position: Int) {
                Timber.i("$position")
                with(binding){
                    val itm = viewModel.bookList.value?:return
                    this.detailsAuthorTV.text = itm[position].volumeInfo.authors[0]
                    this.detailsTitleTV.text =  viewModel.bookList.value?.get(position)?.volumeInfo?.title
                    this.detailsDescriptionTV.text = itm[position].volumeInfo.description

                }
            }
        }


        val adap = AuthorDetailsAdapter()
        val snap = PagerSnapHelper()
        val listener = SnapOnScrollListener(snap, SnapOnScrollListener.Behavior.NOTIFY_ON_SCROLL_STATE_IDLE,list)

        with(binding) {
            snap.attachToRecyclerView(this.recyclerView)
            this.recyclerView.apply {
                layoutManager = CustomLayout(ctx)
                //layoutManager = LinearLayoutManager(ctx, LinearLayoutManager.HORIZONTAL, false)
                snap.findSnapView(layoutManager)
                adapter = adap
                addOnScrollListener(listener)
            }

            this.buyBookTV.text = "Buy this book for $19.99"
            viewModel.bookList.observe(viewLifecycleOwner, {
                adap.updateListing(it)
            })
            this.backButton.setOnClickListener {
                view.findNavController().popBackStack()
            }
        }
    }
}
fun SnapHelper.getSnapPosition(recyclerView: RecyclerView): Int {
    val layoutManager = recyclerView.layoutManager ?: return RecyclerView.NO_POSITION
    val snapView = findSnapView(layoutManager) ?: return RecyclerView.NO_POSITION
    return layoutManager.getPosition(snapView)
}