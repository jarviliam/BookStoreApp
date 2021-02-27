package com.example.googlebooksapi.video

import android.content.Context
import android.util.AttributeSet
import android.widget.LinearLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class TabViewer @JvmOverloads constructor(
    context: Context, attrs: AttributeSet?
) : LinearLayout(context, attrs) {

    private val sharedViewPool = RecyclerView.RecycledViewPool()
    private val ad = TabHeaderAdapter(emptyList())
    private val tabRV = RecyclerView(context).apply {
        layoutManager = LinearLayoutManager(context).also {
            it.recycleChildrenOnDetach = true
            orientation = HORIZONTAL
        }
        adapter = ad
    }
    private val itemRV = RecyclerView(context).apply {
        layoutManager = LinearLayoutManager(context).also {
            it.recycleChildrenOnDetach = true
        }
    }

    fun setItems(items: List<Tab>) {
        ad.setItems(items)
    }

}