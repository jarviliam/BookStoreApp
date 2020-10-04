package com.example.googlebooksapi

import android.content.Context
import android.graphics.Point
import android.view.View
import androidx.core.view.marginStart
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import timber.log.Timber
import kotlin.math.abs


class CustomLayout constructor(val context: Context): LinearLayoutManager(context, HORIZONTAL, false) {

    private var currentPos : Int = 0
    private var center : Point = Point(width,height)
    override fun generateDefaultLayoutParams(): RecyclerView.LayoutParams =
        RecyclerView.LayoutParams(
            RecyclerView.LayoutParams.MATCH_PARENT,
            RecyclerView.LayoutParams.WRAP_CONTENT
        )

    override fun onLayoutChildren(recycler: RecyclerView.Recycler, state: RecyclerView.State) {
        if(state.itemCount == 0){
            removeAndRecycleAllViews(recycler)
            currentPos = -1
            return
        }
        super.onLayoutChildren(recycler, state)

        center.x = width/2
        val center = findCurrentCenterView()
        setChildOffSets()
        //detachAndScrapAttachedViews(recycler)
        //fill()
    }
    private fun resolveOffset(xPos: Double, center: Point, peekDistance : Int): Double{
       val adj = Math.abs(center.x - xPos)
        return adj

    }


    private fun setChildOffSets(){
        Timber.i("Child Count : $childCount")
        for (x in 0..childCount){
            val child = getChildAt(x)?: continue
            val layoutParams = child.layoutParams
            if(x == 0 || x == 2){
                val childCenter =  (child.x + child.width ) /2.0f
                val distanceFromCenter = abs(childCenter - center.x)
                val yVal = child.marginStart - distanceFromCenter
                val dimen = context.resources.getDimension(R.dimen.bookDetailOtherWidth)
                val diff = child.width - dimen / 2
                child.scaleY = 0.7f
                child.scaleX = 0.7f
            }
            else if(x == 1){
                //val dimen = context.resources.getDimension(R.dimen.bookDetailWidth)
                child.scaleX = 1f
                child.scaleY = 1f
            }
            Timber.i("Child Layout params ${x}: ${layoutParams.height}")
        }

    }

    override fun scrollHorizontallyBy(
        dx: Int,
        recycler: RecyclerView.Recycler?,
        state: RecyclerView.State?
    ): Int {
        val xBy = super.scrollHorizontallyBy(dx, recycler, state)
        Timber.i("xBy : $xBy , State: $state")
        setChildOffSets()
        return xBy
    }
    private fun findCurrentCenterView(): View? {
        // +++++ prepare data +++++

        // center of the screen in x-axis
        val centerX = width / 2f
        val viewHalfWidth: Float = 120f / 2f
        var nearestToCenterView: View? = null
        var nearestDeltaX = 0
        var centerXView: Int
        // ----- prepare data -----

        // search nearest to center view
        val count = childCount
        var i = 0
        while (i < count) {
            val item = getChildAt(i)
            centerXView = (getDecoratedLeft(item!!) + viewHalfWidth).toInt()
            if (nearestToCenterView == null || Math.abs(nearestDeltaX) > abs(centerX - centerXView)) {
                nearestToCenterView = item
                nearestDeltaX = (centerX - centerXView).toInt()
            }
            i++
        }
        return nearestToCenterView
    }
}