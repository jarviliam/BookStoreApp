package com.example.googlebooksapi.UI.home


import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.googlebooksapi.aou.ItemObj
import com.example.googlebooksapi.databinding.BookItemBinding
import com.example.googlebooksapi.load
import timber.log.Timber
import javax.inject.Inject

class BooksAdapter @Inject constructor() :
    RecyclerView.Adapter<BooksAdapter.ViewHolder>() {

    private var list: List<ItemObj> = emptyList()

    fun updateList(l: List<ItemObj>) {
        list = l
        notifyDataSetChanged()
    }

    class ViewHolder(private val bookBinding: BookItemBinding) :
        RecyclerView.ViewHolder(bookBinding.root) {
        fun bind(item: ItemObj) {
            with(bookBinding) {
                Timber.i("Loading Binding")
                this.bookItemAuthor.text = item.volumeInfo.authors.toString()
                this.bookItemTitle.text = item.volumeInfo.title
                if(item.volumeInfo.imageLinks !== null){

                    this.bookImage.load(item.volumeInfo.imageLinks.thumbnail)
                }
                //this.bookImage.load(item.volumeInfo.imageLinks.thumbnail)

            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            BookItemBinding.inflate(
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