package com.example.googlebooksapi

import android.content.Context
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import timber.log.Timber
import kotlin.math.abs


class CustomLayout constructor(val context: Context): LinearLayoutManager(context, HORIZONTAL, false) {

    private var currentPos : Int = 0
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

        //super.onLayoutChildren(recycler, state)
        val center = findCurrentCenterView()
        Timber.i("Center ${center?.id}")
        setChildOffSets()
        //detachAndScrapAttachedViews(recycler)
        //fill()
    }
    private fun setChildOffSets(){
        Timber.i("Child Count : $childCount")
        for (x in 0..childCount){
            val child = getChildAt(x)?: continue
            val layoutParams = child.layoutParams
            Timber.i("Height : ${this.height}")
            if(x == 0 || x == 2){
                Timber.i("Pos 0 : ${child.top}")

                val dimen = context.resources.getDimension(R.dimen.bookDetailOtherWidth)
                Timber.i("Dimension : ${dimen}, Actual :${child.width}")
                val diff = child.width - dimen / 2
                Timber.i("Right : ${child.right}, Diff: ${diff}")
                //child.layout((child.left+diff).toInt(), child.top, (child.right-diff).toInt(),child.bottom-5)
            }
            else if(x == 1){
                val dimen = context.resources.getDimension(R.dimen.bookDetailWidth)
                child.layout(child.left, child.top, child.right, child.bottom)
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