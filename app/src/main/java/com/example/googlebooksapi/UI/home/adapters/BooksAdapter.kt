package com.example.googlebooksapi.UI.home.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.example.googlebooksapi.aou.ItemObj
import com.example.googlebooksapi.aou.Temp
import com.example.googlebooksapi.databinding.BookItemBinding
import timber.log.Timber
import javax.inject.Inject

class BooksAdapter @Inject constructor(private val clicker: (item: ItemObj, imgVW: ImageView) -> Unit) :
    RecyclerView.Adapter<BooksAdapter.ViewHolder>() {

    private var list: List<Temp> = emptyList()

    fun updateList(l: List<Temp>) {
        list = l
        notifyDataSetChanged()
    }

    class ViewHolder(
        private val binding: BookItemBinding,
        private val onItemClicked: (item: ItemObj, imgVW: ImageView) -> Unit
    ) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Temp) {
            binding.root.setBackgroundColor(item.color)
            /*with(bookBinding) {
                Timber.i("Loading Binding")
                this.root.transitionName = "book-${item.id}"
                this.bookItem.setOnClickListener { onItemClicked(item, bookBinding.bookImage) }
                this.bookItemAuthor.text = item.volumeInfo.authors[0]
                this.bookItemTitle.text = item.volumeInfo.title
                if (item.volumeInfo.imageLinks !== null) {
                    Timber.i(item.volumeInfo.imageLinks.thumbnail)

                    this.placeHolderImg.visibility = View.INVISIBLE
                }
            }*/
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            BookItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            ), clicker
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        return holder.bind(list[position])
    }

    override fun getItemCount(): Int = list.size

}