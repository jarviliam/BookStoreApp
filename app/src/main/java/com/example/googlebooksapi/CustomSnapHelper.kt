package com.example.googlebooksapi

import android.view.View
import androidx.recyclerview.widget.LinearSnapHelper
import androidx.recyclerview.widget.OrientationHelper
import androidx.recyclerview.widget.RecyclerView


class CustomSnapHelper : LinearSnapHelper() {
    override fun calculateDistanceToFinalSnap(
        layoutManager: RecyclerView.LayoutManager,
        targetView: View
    ): IntArray? {
        TODO()
    }
    private fun distanceToCenter(
        layoutManager: RecyclerView.LayoutManager,
        targetView: View, helper: OrientationHelper
    ): Int {
        val childCenter = (helper.getDecoratedStart(targetView)
                + helper.getDecoratedMeasurement(targetView) / 2)
        val containerCenter: Int = if (layoutManager.clipToPadding) {
            helper.startAfterPadding + helper.totalSpace / 2
        } else {
            helper.end / 2
        }
        return childCenter - containerCenter
    }

    override fun findSnapView(layoutManager: RecyclerView.LayoutManager?): View? {
        TODO("Not yet implemented")
    }

    override fun findTargetSnapPosition(
        layoutManager: RecyclerView.LayoutManager?,
        velocityX: Int,
        velocityY: Int
    ): Int {
        TODO("Not yet implemented")
    }
}