package com.example.googlebooksapi.UI.home.adapters

import android.view.View
import android.view.ViewGroup
import androidx.core.view.ViewCompat
import androidx.recyclerview.widget.LinearSmoothScroller
import androidx.recyclerview.widget.OrientationHelper
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.currentCoroutineContext
import timber.log.Timber
import kotlin.math.*

class SingleLayoutManager : RecyclerView.LayoutManager() {
    private var _pendingScrollPosition: Int? = null
    private var _itemSizeWidth: Int? = null
    private var _itemCount: Int? = null
    private var _itemSizeHeight: Int? = null
    private var _currentSelected: Int? = 1
    private val helper = LayoutHelper()

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
        Timber.i("Position ${position}")
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

    override fun scrollHorizontallyBy(
        dx: Int,
        recycler: RecyclerView.Recycler,
        state: RecyclerView.State
    ): Int {
        return scrollBy(dx, recycler, state)
    }

    private fun scrollBy(
        diff: Int,
        recycler: RecyclerView.Recycler,
        state: RecyclerView.State
    ): Int {
        if (_itemSizeWidth == null) return 0
        if (childCount == 0 || diff == 0) return 0
        Timber.i("SCROLL BY")
        val maxOffset = getMaxScrollOffset()
        Timber.i("Max Offset ${maxOffset}")
        Timber.i("Diff${diff}")
        //_currentSelected = _currentSelected?.plus(1)
        val scrolLAmount = if (0 > helper._scrollOffset + diff) {
            Timber.i("RESTTTO 0")
            -helper._scrollOffset
        } else if (helper._scrollOffset + diff > maxOffset) {
            Timber.i("MAKE MAX OFFSET")
            maxOffset - helper._scrollOffset
        } else {
            Timber.i("Man Just the Diff?")
            diff
        }
        if (scrolLAmount != 0) {
            helper._scrollOffset += scrolLAmount
            fillData(recycler, state)
        }
        return scrolLAmount
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
                Timber.i("List is Not Empty")
                list[0].itemView
            }
            measureChildWithMargins(view!!, 0, 0)
            val width = getDecoratedMeasuredWidth(view)
            val height = getDecoratedMeasuredHeight(view)
            if (shouldRecycle) {
                detachAndScrapView(view, recycler)
            }
            if (_pendingScrollPosition == null) {
                _pendingScrollPosition = _currentSelected
            }
            _itemSizeWidth = width
            _itemSizeHeight = height

        }
        if (_pendingScrollPosition != null) {
            Timber.i("Pending Scroll Pos is Not NUll")
            helper._scrollOffset = calculateScrollForSelected(_pendingScrollPosition!!, state)
        }
        if (state.didStructureChange() && _currentSelected != null) {
            Timber.i("Current Not null. Structure Change")
            helper._scrollOffset = calculateScrollForSelected(_currentSelected!!, state)
        }

        fillData(recycler, state)
    }

    private fun fillData(recycler: RecyclerView.Recycler, state: RecyclerView.State) {
        makeLayoutOrder(state)
        val currentScrollPos = getCurrentScrollPosition()
        Timber.i("Current Scroll Pos${currentScrollPos}")
        detachAndScrapAttachedViews(recycler)
        val width = width - paddingStart - paddingEnd
        val height = height - paddingTop - paddingBottom

        val top = (height - _itemSizeHeight!!) / 2
        val bottom = top + _itemSizeHeight!!

        val center = (width - _itemSizeWidth!!) / 2
        //for (i in 2 downTo 0) {
        helper.getOrder().forEachIndexed { i, layoutOrder ->
            val offCenterPos = i - _currentSelected!!
            val offset = getOffsetByPosition(layoutOrder.itemPositionDiff)
            val start = center + offset
            val end = start + _itemSizeWidth!!
            Timber.i("View ${i} - ${offset} ${start} ${end}")
            fillView(layoutOrder, recycler, start, top, end, bottom)
        }

        val absScrolLpos = makeScrollPositionInRange(currentScrollPos, state.itemCount)
        val centerItem = absScrolLpos.roundToInt()

        Timber.i("Center Pos ${centerItem}")

        recycler.clear()
        detectSelectedChanged(currentScrollPos, state)

    }

    private fun fillView(
        order: LayoutOrder,
        recycler: RecyclerView.Recycler,
        start: Int,
        top: Int,
        end: Int,
        bottom: Int
    ) {
        val view = recycler.getViewForPosition(order.adapterPos)
        addView(view)
        measureChildWithMargins(view, 0, 0)
        val elevation = if (order.itemPositionDiff == 0F) 10F else order.itemPositionDiff.toFloat()
        ViewCompat.setElevation(view, elevation)
        view.layout(start, top, end, bottom)
    }

    private fun makeLayoutOrder(state: RecyclerView.State) {
        //Generate Views
        _itemCount = state.itemCount
        val absCurrentScrollpos =
            makeScrollPositionInRange(_currentSelected!!.toFloat(), _itemCount!!)
        val center = absCurrentScrollpos.roundToInt()
        //Last Draw = Center Item
        val drawCount = if (_currentSelected == 0) 2 else 3
        val firstDraw = max(center - drawCount, 0)
        val lastDraw = min(center + drawCount, drawCount - 1)
        val layout = lastDraw - firstDraw + 1
        Timber.i(" Last Draw${lastDraw}")
        Timber.i("Layou Count ${layout}")
        for (i in firstDraw..lastDraw) {
            if (i == center) {
                Timber.i("Center here")
                helper.addOrderItem(layout - 1, i, 0F)
            } else if (i < center) {
                helper.addOrderItem(i - firstDraw, i, i - absCurrentScrollpos)
            } else {
                helper.addOrderItem(layout - (i - center), i, i - absCurrentScrollpos)
            }
        }
    }

    private fun getOffsetByPosition(itemPosition: Float): Int {
        val smoothPosition = convertItemPositionDiffToSmoothPositionDiff(itemPosition)

        val diff = (width - paddingStart - paddingEnd) - _itemSizeWidth!! / 2
        return (sign(itemPosition) * diff * smoothPosition).roundToInt()
    }

    private fun calculateScrollForSelected(itemPos: Int, state: RecyclerView.State): Int {
        if (itemPos == null) {
            return 0
        }
        val fixedPosition = if (itemPos < state.itemCount) itemPos else itemCount - 1
        return fixedPosition * _itemSizeWidth!!
    }


    private fun convertItemPositionDiffToSmoothPositionDiff(itemPositionDiff: Float): Double {
        //Obtain Absolute Value
        val absIemPositionDiff = abs(itemPositionDiff)
        //val absIemPositionDiff = abs(itemPositionDiff - _currentSelected!!)
        Timber.i("Abs Item Diff ${absIemPositionDiff}")
        val drawCount = if (_currentSelected == 0) 2 else 3

        // we detect if this item is close for center or not. We use (1 / maxVisibleItem) ^ (1/3) as close definer.
        return if (absIemPositionDiff > StrictMath.pow(
                (1.0f / drawCount).toDouble(),
                (1.0f / drawCount).toDouble()
            )
        ) {
            Timber.i("This Equation")

            //2.0
            // this item is far from center line, so we should make it move like square root function
            StrictMath.pow(
                absIemPositionDiff / drawCount.toDouble(),
                (1 / 2.0f).toDouble()
            )
        } else {
            Timber.i("That equation")
            //5.0
            //0.0
            StrictMath.pow(absIemPositionDiff.toDouble(), 2.0)
        }
    }

    private fun detectSelectedChanged(currentScrollPos: Float, state: RecyclerView.State) {
        val absScrollPosition = makeScrollPositionInRange(currentScrollPos, state.itemCount)
        val centerItem = absScrollPosition.roundToInt()
        if (_currentSelected != centerItem) {
            _currentSelected = centerItem
        }
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
        } else 1.0f / getScrollItemSize()
    }

    private fun getMaxScrollOffset(): Int {
        Timber.i("Item count ${_itemCount}")

        return if (_itemCount == null) {
            getScrollItemSize() * -1
        } else {
            getScrollItemSize() * (_itemCount!! - 1)
        }
    }

    /**
     * @return Item Size
     */
    private fun getScrollItemSize(): Int {
        return _itemSizeWidth ?: 0
    }

    inner class LayoutHelper() {
        private val order = mutableListOf<LayoutOrder>()
        var _scrollOffset = 0

        fun addOrderItem(pos: Int, adapterPos: Int, itemDiff: Float) {
            if (order.size <= pos) {
                order.add(LayoutOrder(adapterPos, itemDiff))
            } else {
                order.set(pos, LayoutOrder(adapterPos, itemDiff))
            }
            Timber.i("SIZE ${order.size}")
        }

        fun getOrder() = order
    }

    companion object {
        private const val HORIZONTAL = OrientationHelper.HORIZONTAL

        data class LayoutOrder(val adapterPos: Int, val itemPositionDiff: Float)

        private fun makeScrollPositionInRange(currentScrollPos: Float, count: Int): Float {
            var absoluteCurrent = currentScrollPos
            while (0 > absoluteCurrent) {
                absoluteCurrent += count
            }
            while (absoluteCurrent.roundToInt() >= count) {
                absoluteCurrent -= count
            }
            Timber.i("Absolute Current ${absoluteCurrent}")
            return absoluteCurrent
        }
    }
}