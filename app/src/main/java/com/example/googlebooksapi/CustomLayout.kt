package com.example.googlebooksapi

import android.content.Context
import android.graphics.Point
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import timber.log.Timber
import kotlin.math.abs


class CustomLayout constructor(val context: Context): LinearLayoutManager(context, HORIZONTAL, false) {

    private var currentPos: Int = 0
    private var center: Point = Point(width, height)
    override fun generateDefaultLayoutParams(): RecyclerView.LayoutParams =
        RecyclerView.LayoutParams(
            RecyclerView.LayoutParams.MATCH_PARENT,
            RecyclerView.LayoutParams.WRAP_CONTENT
        )

    override fun onLayoutChildren(recycler: RecyclerView.Recycler, state: RecyclerView.State) {
        if (state.itemCount == 0) {
            removeAndRecycleAllViews(recycler)
            currentPos = -1
            return
        }
        super.onLayoutChildren(recycler, state)

        Timber.tag("LayoutChildren ")
        Timber.i("Called")
        center.x = width / 2
        setChildOffSets()
    }

    private fun setChildOffSets() {
        Timber.i("Child Count : $childCount")
        val midpoint = center.x
        val d1 = 0.9f * midpoint
        for (x in 0..childCount) {
            val child = getChildAt(x) ?: continue
            val childMidpoint = (getDecoratedRight(child) + getDecoratedLeft(child)) / 2f
            val d = d1.coerceAtMost(abs(midpoint - childMidpoint))
            val scale = 1f + (0.70f - 1f) * (d - 0f) / (d1 - 0f)
            child.scaleX = scale
            child.scaleY = scale
        }
    }

    override fun scrollHorizontallyBy(
        dx: Int,
        recycler: RecyclerView.Recycler?,
        state: RecyclerView.State?
    ): Int {
        val xBy = super.scrollHorizontallyBy(dx, recycler, state)
        setChildOffSets()
        return xBy
    }
}
