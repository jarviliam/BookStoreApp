package com.example.googlebooksapi.UI.home.adapters

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearSmoothScroller
import androidx.recyclerview.widget.OrientationHelper
import androidx.recyclerview.widget.RecyclerView
import timber.log.Timber
import kotlin.math.roundToInt

class SingleLayoutManager : RecyclerView.LayoutManager() {
    private var _pendingScrollPosition: Int? = null
    private var _itemSizeWidth: Int? = null
    private var _itemCount: Int? = null

    override fun generateDefaultLayoutParams(): RecyclerView.LayoutParams {
        Timber.i("Generating Default")
        return RecyclerView.LayoutParams(
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
    }

    fun getOrientation(): Int {
        return HORIZONTAL
    }

    override fun canScrollHorizontally(): Boolean {
        return childCount != 0
    }

    override fun canScrollVertically(): Boolean {
        return false
    }

    /**
     * Throws an Exception on Negatives.
     * Requests Layout to be re-rendered
     */
    override fun scrollToPosition(position: Int) {
        if (position < 0) {
            throw IllegalArgumentException("DANG BRO")
        }
        _pendingScrollPosition = position
        requestLayout()
    }

    override fun smoothScrollToPosition(
        recyclerView: RecyclerView,
        state: RecyclerView.State,
        position: Int
    ) {
        val scroller = object : LinearSmoothScroller(recyclerView.context) {
            override fun calculateDxToMakeVisible(view: View, snapPreference: Int): Int {
                return calculateOffset(view)
            }

            override fun calculateDyToMakeVisible(view: View?, snapPreference: Int): Int {
                return 0
            }
        }
        scroller.targetPosition = position
        startSmoothScroll(scroller)
    }

    override fun onLayoutChildren(recycler: RecyclerView.Recycler, state: RecyclerView.State) {
        if (state.itemCount == 0) {
            removeAndRecycleAllViews(recycler)
            Timber.e("No Items")
            return
        }
        detachAndScrapAttachedViews(recycler)
        if (_itemSizeWidth == null) {
            val list = recycler.scrapList
            var shouldRecycle = false
            var view: View? = if (list.isEmpty()) {
                Timber.i("List is Empty")
                shouldRecycle = true
                val x = recycler.getViewForPosition(
                    if (_pendingScrollPosition == null) 0 else 0.coerceAtLeast(
                        (itemCount - 1).coerceAtMost(_pendingScrollPosition!!)
                    )
                )
                addView(x)
                x
            } else {
                list[0].itemView
            }
            measureChildWithMargins(view!!, 0, 0)
            val width = getDecoratedMeasuredWidth(view)
            val height = getDecoratedMeasuredHeight(view)
            Timber.i("WIDTH ${width}")
            if (shouldRecycle) {
                detachAndScrapView(view, recycler)
            }
            _itemSizeWidth = width

        }
        //super.onLayoutChildren(recycler, state)
        detachAndScrapAttachedViews(recycler)
        //recycler.clear()
    }

    private fun calculateOffset(view: View): Int {
        val target = getPosition(view)
        val distance = getScrollDirection(target)
        return (distance * getScrollItemSize()).roundToInt()
    }

    private fun getScrollDirection(targetPos: Int): Float {
        val currentScrollPos = makeScrollPositionInRange(getCurrentScrollPosition(), itemCount)
        return currentScrollPos - targetPos
    }

    private fun getCurrentScrollPosition(): Float {
        val fullScrollSize: Int = getMaxScrollOffset()
        return if (0 == fullScrollSize) {
            0f
        } else 1.0f
    }

    private fun getMaxScrollOffset(): Int {
        return getScrollItemSize() * (_itemCount ?: 0 - 1)
    }

    /**
     * @return Item Size
     */
    private fun getScrollItemSize(): Int {
        return _itemSizeWidth ?: 0
    }

    companion object {
        private const val HORIZONTAL = OrientationHelper.HORIZONTAL

        private fun makeScrollPositionInRange(currentScrollPos: Float, count: Int): Float {
            var absoluteCurrent = currentScrollPos
            while (0 > absoluteCurrent) {
                absoluteCurrent += count
            }
            while (absoluteCurrent.roundToInt() >= count) {
                absoluteCurrent -= count
            }
            return absoluteCurrent
        }
    }
}