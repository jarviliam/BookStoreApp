package com.example.googlebooksapi.UI.audiobookDetail

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import androidx.fragment.app.viewModels
import com.example.googlebooksapi.R
import com.example.googlebooksapi.databinding.AudioBookDetailFragmentBinding
import timber.log.Timber

class AudioBookDetailFragment : Fragment(R.layout.audio_book_detail_fragment) {

    private val viewModel by viewModels<AudioBookDetailViewModel>()


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        super.onViewCreated(view, savedInstanceState)
        Timber.tag("Audio Book Detail")

        Timber.i("On View Created")

        val binding = AudioBookDetailFragmentBinding.bind(view)

        with(binding) {


        }
    }
}