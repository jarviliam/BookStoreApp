package com.example.googlebooksapi.UI.home.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.googlebooksapi.aou.ItemObj
import com.example.googlebooksapi.databinding.AudiobookItemBinding
import timber.log.Timber

class AudioBooksAdapter constructor() : RecyclerView.Adapter<AudioBooksAdapter.ViewHolder>() {

    private var list: List<ItemObj> = emptyList()

    fun updateListings(l: List<ItemObj>) {
        list = l
        notifyDataSetChanged()
    }

    class ViewHolder(private val binding : AudiobookItemBinding) : RecyclerView.ViewHolder(binding.root){

        fun bind(item: ItemObj){
            Timber.i("Audio Book Binding")
            with(binding){
                if(item.volumeInfo.imageLinks !== null) {
                    //this.audioBookImage.load(item.volumeInfo.imageLinks.thumbnail)
                }

            }

        }

    }
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        return ViewHolder(
            AudiobookItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )


    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        return holder.bind(list[position])
    }

    override fun getItemCount(): Int = list.size

}