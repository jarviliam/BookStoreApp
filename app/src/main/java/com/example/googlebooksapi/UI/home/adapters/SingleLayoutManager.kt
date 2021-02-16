package com.example.googlebooksapi.UI.home.adapters

import android.animation.ValueAnimator
import android.graphics.PointF
import android.view.View
import android.view.ViewGroup
import android.view.animation.LinearInterpolator
import androidx.core.view.ViewCompat
import androidx.recyclerview.widget.LinearSmoothScroller
import androidx.recyclerview.widget.OrientationHelper
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.currentCoroutineContext
import timber.log.Timber
import kotlin.math.*

class SingleLayoutManager : RecyclerView.LayoutManager(),
    RecyclerView.SmoothScroller.ScrollVectorProvider {
    private var _pendingScrollPosition: Int? = null
    private var _itemSizeWidth: Int? = null
    private var _itemCount: Int? = null
    private var _itemSizeHeight: Int? = null
    private var _currentSelected: Int? = 0
    private val _autoMinAnim = 100
    private var _SelectedAnimator: ValueAnimator? = null
    private val _autoMaxAnim = 300
    private var _isAutoSelected: Boolean = true
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
        _pendingScrollPosition = position
        requestLayout()
    }


    override fun onScrollStateChanged(state: Int) {
        super.onScrollStateChanged(state)
        Timber.i("------------")
        Timber.i("SCROLL STATE ${state}")
        when (state) {
            RecyclerView.SCROLL_STATE_DRAGGING -> {
                //Cancel Animation
                cancelAnimator()
            }
            RecyclerView.SCROLL_STATE_IDLE -> {
                if (_isAutoSelected) {
                    testScrollAnimate()
                }
            }
        }
    }

    private fun shouldFindSelectPosition() {
        val remainder = (abs(helper._scrollOffset) % getMaxScrollOffset()).toInt()
        if (remainder >= getMaxScrollOffset() / 2.0f) {

        }
    }

    private fun testScrollAnimate() {
        cancelAnimator()
        val x = getOffsetFromCenterView()
        val distance = getOffsetByPosition(getCurrentScrollPosition())
        //val duration =
        _SelectedAnimator = ValueAnimator.ofInt(0, distance)
        _SelectedAnimator?.setDuration(300)
        _SelectedAnimator?.setInterpolator(LinearInterpolator())
        val startedOffset = helper._scrollOffset
        Timber.tag("Animator")
        Timber.i("----------------------")
        Timber.i("Current Selected ${_currentSelected}")
        Timber.i("Offset From Center: ${x}")
        Timber.i("Distance From Center: ${distance}")
        Timber.i("Started Offset: ${startedOffset}")
        _SelectedAnimator?.addUpdateListener {
            Timber.tag("Animation")
            Timber.i("Scroll Offset ${helper._scrollOffset}")
            Timber.i("Value ${it.animatedValue}")
            if (helper._scrollOffset < 0) {
            } else {
                helper._scrollOffset += (startedOffset + (it.animatedValue as Int))
            }
            requestLayout()
        }
        _SelectedAnimator?.start()
    }

    private fun cancelAnimator() {
        if (_SelectedAnimator != null && (_SelectedAnimator!!.isStarted || _SelectedAnimator!!.isRunning)) {
            _SelectedAnimator?.cancel()
        }
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
        val scrolLAmount = if (0 > helper._scrollOffset + diff) {
            -helper._scrollOffset
        } else if (helper._scrollOffset + diff > maxOffset) {
            maxOffset - helper._scrollOffset
        } else {
            diff
        }
        if (scrolLAmount != 0) {
            helper._scrollOffset += scrolLAmount
            Timber.i("Scroll By")
            fillData(recycler, state)
        }
        return scrolLAmount
    }

    override fun onLayoutChildren(recycler: RecyclerView.Recycler, state: RecyclerView.State) {
        if (state.itemCount == 0) {
            removeAndRecycleAllViews(recycler)
            return
        }
        Timber.i("Width ${width - paddingEnd - paddingStart}")
        Timber.i("Width ${_itemSizeWidth}")
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
        Timber.i("Layout Children")
        if (_pendingScrollPosition != null) {
            //helper._scrollOffset = calculateScrollForSelected(_pendingScrollPosition!!, state)
            _pendingScrollPosition = null
        }
        if (state.didStructureChange() && _currentSelected != null) {
            //helper._scrollOffset = calculateScrollForSelected(_currentSelected!!, state)
        }

        fillData(recycler, state)
    }

    private fun fillData(recycler: RecyclerView.Recycler, state: RecyclerView.State) {
        makeLayoutOrder(state)
        detachAndScrapAttachedViews(recycler)

        val fraction =
            (abs(helper._scrollOffset) % getMaxScrollOffset()) / (getMaxScrollOffset() * 1.0F)
        Timber.i("Fraction ${fraction}")
        //val firstView = floor((abs(helper._scrollOffset) / getMaxScrollOffset()).toDouble()).roundToInt()
        //Get Width of recycler
        val width = width - paddingStart - paddingEnd
        //Get Height
        val height = height - paddingTop - paddingBottom

        val top = (height - _itemSizeHeight!!) / 2
        val bottom = top + _itemSizeHeight!!
        val center = (width - _itemSizeWidth!!) / 2
        detectSelectedChanged(getCurrentScrollPosition(), state)
        helper.getOrder().forEachIndexed { i, layoutOrder ->
            val offset = getOffsetByPosition(layoutOrder.itemPositionDiff) - helper._scrollOffset
            Timber.i("Offset ${i} ${offset} ${center} ${layoutOrder.adapterPos}")
            val start = center + offset
            val end = start + _itemSizeWidth!!
            Timber.i("View ${i} - ${offset} ${start} ${end}")
            fillView(layoutOrder, recycler, start, top, end, bottom)
        }
        recycler.clear()
        //Update Selected Change if it has changed
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
        //Timber.tag("Fill View")
        //Timber.i("View ${order.adapterPos} - ${transformation.translateX} ${transformation.scaleX}")
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
        Timber.i("Abs Current ${absCurrentScrollpos}")
        val center = absCurrentScrollpos.roundToInt()
        //TODO : Figure out how to make it so that first and last are 2
        val drawCount = if (center == 0) 3 else 3
        //First Draw Position, Default to 0
        val firstDraw = max(center - drawCount, 0)
        //Last Draw Position, Default to Max - 1
        val lastDraw = min(center + drawCount, itemCount - 1)
        //Calculate the Nmber of layouts
        val layout = lastDraw - firstDraw + 1
        Timber.i("Layout Count ${layout}")
        Timber.i("Center ${center}")

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

    fun getSelectedItemPosition() {

    }

    /**
     * Scrolls Offset from the nearest item to center
     */
    fun getOffsetFromCenterView(): Int {
        return (getCurrentScrollPosition() * getScrollItemSize() - helper._scrollOffset).roundToInt()
    }

    private fun getOffsetForView(view: View): Int {
        Timber.i("Offset for View")
        val targetPosition = getPosition(view)
        val distance = getScrollDirection(targetPosition)
        return (distance * getScrollItemSize()).roundToInt()
    }

    private fun getOffsetByPosition(itemPosition: Float): Int {
        val smoothPosition = convertItemPositionDiffToSmoothPositionDiff(itemPosition)
        Timber.i("Smoothing ${itemPosition} ${smoothPosition}")
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
        val drawCount = 3

        // we detect if this item is close for center or not. We use (1 / maxVisibleItem) ^ (1/3) as close definer.
        return if (absIemPositionDiff > StrictMath.pow(
                (1.0f / drawCount).toDouble(),
                (1.0f / 3.0)
            )
        ) {
            // this item is far from center line, so we should make it move like square root function
            StrictMath.pow(
                absIemPositionDiff / drawCount.toDouble(),
                (1 / 2.0f).toDouble()
            )
        } else {
            //5.0
            //0.0
            StrictMath.pow(absIemPositionDiff.toDouble(), 2.0)
        }
    }

    private fun detectSelectedChanged(currentScrollPos: Float, state: RecyclerView.State) {
        Timber.i("Selected Changed")
        val absScrollPosition = makeScrollPositionInRange(currentScrollPos, state.itemCount)
        val centerItem = absScrollPosition.roundToInt()
        if (_currentSelected != centerItem) {
            Timber.i("ReCentering to -> ${centerItem}")
            //_currentSelected = centerItem
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
            val scale = 1.0f - scaleMultiple * abs(differenceToCenter).roundToInt()
            val translateX = view.measuredWidth * (1 - scale) / 2f
            return ItemTransform(scale, scale, translateX)
        }
    }

    companion object {
        private const val HORIZONTAL = OrientationHelper.HORIZONTAL

        data class LayoutOrder(val adapterPos: Int, val itemPositionDiff: Float)
        data class ItemTransform(val scaleX: Float, val scaleY: Float, val translateX: Float)

        private fun calculateCenterViewPosition(currentScrollPos: Float, count: Int): Int {
            var absoluteCurrent = currentScrollPos
            while (0 > absoluteCurrent) {
                absoluteCurrent += count
            }
            while (absoluteCurrent.roundToInt() >= count) {
                absoluteCurrent -= count
            }
            return absoluteCurrent.roundToInt()
        }

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

    override fun computeScrollVectorForPosition(targetPosition: Int): PointF? {
        if (childCount == 0) return null
        val directionDistance = getScrollDirection(targetPosition)
        val direction = -sign(directionDistance)
        return PointF(direction, 0F)
    }
}