package com.example.googlebooksapi.video

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.googlebooksapi.databinding.TabItemBinding

class TabHeaderAdapter constructor(private var items: List<Tab>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val binding = TabItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TabHeaderViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as TabHeaderViewHolder).bind(items[position])
    }

    override fun getItemCount(): Int {
        return items.size
    }

    inner class TabHeaderViewHolder constructor(private val binding: TabItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: Tab) {
            binding.tabTitle.text = item.text
        }
    }

    fun setItems(newI: List<Tab>) {
        items = newI
    }
}