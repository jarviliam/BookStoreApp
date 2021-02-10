package com.example.googlebooksapi.ext

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.view.View
import android.view.ViewPropertyAnimator
import android.widget.TextView
import androidx.core.animation.doOnEnd


fun View.visible() {
    if (visibility != View.VISIBLE) visibility = View.VISIBLE
}

fun View.gone() {
    if (visibility != View.GONE) visibility = View.GONE
}

fun View.invisible() {
    if (visibility != View.INVISIBLE) visibility = View.INVISIBLE
}

fun View.visibleIf(condition: Boolean, gone: Boolean = true) =
    if (condition) {
        visible()
    } else {
        if (gone) gone() else invisible()
    }

fun View.crossfadeTo(view: View, duration: Long = 250) {
    fadeOut(duration)
    view.fadeIn(duration)
}

fun View.fadeIf(condition: Boolean, duration: Long = 250, startDelay: Long = 0) =
    if (condition) {
        fadeIn(duration, startDelay)
    } else {
        fadeOut(duration, startDelay)
    }

fun View.fadeIn(duration: Long = 250, startDelay: Long = 0, endAction: () -> Unit = {}): ViewPropertyAnimator? {
    if (visibility == View.VISIBLE) {
        endAction()
        return null
    }
    visibility = View.VISIBLE
    alpha = 0F
    val animation = animate().alpha(1F).setDuration(duration).setStartDelay(startDelay).withEndAction(endAction)
    return animation.also { it.start() }
}

fun View.fadeOut(duration: Long = 250, startDelay: Long = 0, endAction: () -> Unit = {}): ViewPropertyAnimator? {
    if (visibility == View.GONE) {
        endAction()
        return null
    }
    val animation = animate().alpha(0F).setDuration(duration).setStartDelay(startDelay).withEndAction {
        gone()
        endAction()
    }
    return animation.also { it.start() }
}

fun ViewPropertyAnimator?.add(animations: MutableList<ViewPropertyAnimator?>) {
    animations.add(this)
}

fun View.shake() = ObjectAnimator.ofFloat(this, "translationX", 0F, -15F, 15F, -10F, 10F, -5F, 5F, 0F)
    .setDuration(500)
    .start()

fun View.bump(action: () -> Unit = {}) {
    val x = ObjectAnimator.ofFloat(this, "scaleX", 1F, 1.1F, 1F)
    val y = ObjectAnimator.ofFloat(this, "scaleY", 1F, 1.1F, 1F)

    AnimatorSet().apply {
        playTogether(x, y)
        duration = 250
        doOnEnd { action() }
        start()
    }
}
fun TextView.setTextFade(text: String, duration: Long = 125) {
    fadeOut(
        duration = duration,
        endAction = {
            setText(text)
            fadeIn(duration = duration)
        }
    )
}