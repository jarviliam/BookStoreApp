package com.example.googlebooksapi.UI.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.example.googlebooksapi.aou.ItemObj
import com.example.googlebooksapi.databinding.BookItemBinding
import com.example.googlebooksapi.load
import timber.log.Timber
import javax.inject.Inject

class BooksAdapter @Inject constructor(private val clicker: (item: ItemObj, imgVW: ImageView) -> Unit) :
    RecyclerView.Adapter<BooksAdapter.ViewHolder>() {

    private var list: List<ItemObj> = emptyList()

    fun updateList(l: List<ItemObj>) {
        list = l
        notifyDataSetChanged()
    }

    class ViewHolder(private val bookBinding: BookItemBinding, private val onItemClicked: (item: ItemObj, imgVW: ImageView) -> Unit) :
        RecyclerView.ViewHolder(bookBinding.root) {
        fun bind(item: ItemObj) {
            with(bookBinding) {
                Timber.i("Loading Binding")
                this.root.transitionName = "book-${item.id}"
                this.bookItem.setOnClickListener { onItemClicked(item, bookBinding.bookImage)}
                this.bookItemAuthor.text = item.volumeInfo.authors[0]
                this.bookItemTitle.text = item.volumeInfo.title
                if(item.volumeInfo.imageLinks !== null){
                    Timber.i(item.volumeInfo.imageLinks.thumbnail)

                    this.placeHolderImg.visibility = View.INVISIBLE
                    this.bookImage.load(item.volumeInfo.imageLinks.thumbnail)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            BookItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            ),clicker
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        return holder.bind(list[position])
    }

    override fun getItemCount(): Int = list.size

}