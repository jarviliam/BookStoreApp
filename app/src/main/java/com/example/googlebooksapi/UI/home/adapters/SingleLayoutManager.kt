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
    private val postHelper = PostLayoutListener()

    override fun generateDefaultLayoutParams(): RecyclerView.LayoutParams {
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
                return getOffsetForView(view)
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
        val maxOffset = getMaxScrollOffset()
        Timber.i("Max Offset ${maxOffset}")
        Timber.i("Diff${diff}")
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

    /**
     * This is Where the View is actually rendered
     */
    private fun fillView(
        order: LayoutOrder,
        recycler: RecyclerView.Recycler,
        start: Int,
        top: Int,
        end: Int,
        bottom: Int
    ) {
        //Get the View At Adapter Position And Add it
        val view = recycler.getViewForPosition(order.adapterPos)
        addView(view)
        measureChildWithMargins(view, 0, 0)
        //If It's in the center, Set Elevation.
        val elevation = if (order.itemPositionDiff == 0F) 10F else order.itemPositionDiff
        ViewCompat.setElevation(view, elevation)
        //Get Scaling Factors
        val transformation = postHelper.scaleChild(view, order.itemPositionDiff, order.adapterPos)
        Timber.tag("Fill View")
        Timber.i("View ${order.adapterPos} - ${transformation.translateX} ${transformation.scaleX}")
        //If The view is To the left of the Center. Subtract the Translation.
        //to move it left
        if (order.itemPositionDiff.roundToInt() < 0) {
            view.layout(
                (start - transformation.translateX).roundToInt(),
                top,
                (end - transformation.translateX).roundToInt(),
                bottom
            )
        } else {
            //Otherwise add to move it to the right (opening a gap)
            view.layout(
                (start + transformation.translateX).roundToInt(),
                top,
                (end + transformation.translateX).roundToInt(),
                bottom
            )
        }
        //Apply Scaling Factor
        //Center = 1.0
        view.scaleX = transformation.scaleX
        view.scaleY = transformation.scaleY
    }

    /**
     * Produces the Layout Order of Items
     *
     */
    private fun makeLayoutOrder(state: RecyclerView.State) {
        _itemCount = state.itemCount
        val absCurrentScrollpos =
            makeScrollPositionInRange(_currentSelected!!.toFloat(), _itemCount!!)
        val center = absCurrentScrollpos.roundToInt()
        Timber.i("CENTER")
        //TODO : Figure out how to make it so that first and last are 2
        val drawCount = if (center == 0) 3 else 3
        //First Draw Position, Default to 0
        val firstDraw = max(center - drawCount, 0)
        //Last Draw Position, Default to Max - 1
        val lastDraw = min(center + drawCount, drawCount - 1)
        //Calculate the Nmber of layouts
        val layout = lastDraw - firstDraw + 1

        //Organizes the views into the Helper
        for (i in firstDraw..lastDraw) {
            when {
                center > i -> {
                    helper.addOrderItem(i - firstDraw, i, i - absCurrentScrollpos)
                }
                i == center -> {
                    helper.addOrderItem(layout - 1, i, i - absCurrentScrollpos)
                }
                else -> {
                    helper.addOrderItem(layout - (i - center) - 1, i, i - absCurrentScrollpos)
                }
            }
        }
    }

    /**
     * Scrolls Offset from the nearest item to center
     */
    private fun getOffsetFromCenterView(): Int {
        return (getCurrentScrollPosition() * getScrollItemSize() - helper._scrollOffset).roundToInt()
    }

    private fun getOffsetForView(view: View): Int {
        val targetPosition = getPosition(view)
        val distance = getScrollDirection(targetPosition)
        return (distance * getScrollItemSize()).roundToInt()
    }

    private fun getOffsetByPosition(itemPosition: Float): Int {
        Timber.i("GetOffset by Position ${itemPosition}")
        val smoothPosition = convertItemPositionDiffToSmoothPositionDiff(itemPosition)
        Timber.i("Smooth Position ${smoothPosition}")

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
        Timber.i("Selected Changed")
        val absScrollPosition = makeScrollPositionInRange(currentScrollPos, state.itemCount)
        val centerItem = absScrollPosition.roundToInt()
        Timber.i("Center Item ${centerItem} ${absScrollPosition}")
        if (_currentSelected != centerItem) {
            Timber.i("Not equal to Center ${centerItem} ${_currentSelected}")
            _currentSelected = centerItem
        }
    }

    private fun getScrollDirection(targetPos: Int): Float {
        val currentScrollPos = makeScrollPositionInRange(getCurrentScrollPosition(), itemCount)
        return currentScrollPos - targetPos
    }

    private fun getCurrentScrollPosition(): Float {
        val fullScrollSize: Int = getMaxScrollOffset()
        return if (0 == fullScrollSize) {
            0f
        } else 1.0f * helper._scrollOffset / getScrollItemSize()
    }


    private fun getMaxScrollOffset(): Int {
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
                order[pos] = LayoutOrder(adapterPos, itemDiff)
            }
        }

        fun getOrder() = order
    }

    inner class PostLayoutListener {
        private val scaleMultiple = 0.17f
        fun scaleChild(
            view: View,
            differenceToCenter: Float,
            adapterPosition: Int
        ): ItemTransform {
            Timber.i("VIEW _${differenceToCenter}")
            val scale = 1.0f - scaleMultiple * abs(differenceToCenter).roundToInt()
            val translateX = view.measuredWidth * (1 - scale) / 2f
            return ItemTransform(scale, scale, translateX)
        }
    }

    companion object {
        private const val HORIZONTAL = OrientationHelper.HORIZONTAL

        data class LayoutOrder(val adapterPos: Int, val itemPositionDiff: Float)
        data class ItemTransform(val scaleX: Float, val scaleY: Float, val translateX: Float)

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