package com.example.googlebooksapi.UI.audiobookDetail

import android.content.ClipData
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.googlebooksapi.aou.ItemObj
import com.example.googlebooksapi.databinding.BookDetailItemBinding
import com.example.googlebooksapi.load
import timber.log.Timber

class AuthorDetailsAdapter constructor() :
    RecyclerView.Adapter<AuthorDetailsAdapter.AuthorDetailsVH>() {

    private var list: List<ItemObj> = emptyList()
    fun updateListing(l: List<ItemObj>) {
        list = l
        notifyDataSetChanged()
    }

    class AuthorDetailsVH constructor(private val bookBinding: BookDetailItemBinding) :
        RecyclerView.ViewHolder(bookBinding.root) {
        fun bind(itemObj: ItemObj) {
            Timber.i("Binding Author Details Item")
            with(bookBinding) {

                if(itemObj.volumeInfo.imageLinks.thumbnail != null) {
                    this.imageView3.load(itemObj.volumeInfo.imageLinks.thumbnail)
                }
            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AuthorDetailsVH {
        return AuthorDetailsVH(
            BookDetailItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: AuthorDetailsVH, position: Int) {
        return holder.bind(list[position])
    }

    override fun getItemCount(): Int {
        return list.size
    }
}