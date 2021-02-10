package com.example.googlebooksapi.ext

import android.graphics.Color
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast.LENGTH_SHORT
import com.example.googlebooksapi.R
import com.google.android.material.snackbar.BaseTransientBottomBar.LENGTH_INDEFINITE
import com.google.android.material.snackbar.Snackbar

fun ViewGroup.showSnackbar(
    message: String,
    actionText: Int,
    textColor: Int,
    backgroundRes: Int,
    length: Int,
    action: (() -> Unit)? = null
) {
    Snackbar.make(this, message, length).apply {
        view.findViewById<TextView>(com.google.android.material.R.id.snackbar_text)?.let {
            it.maxLines = 5
        }
        view.setBackgroundResource(backgroundRes)
        setTextColor(textColor)
        setActionTextColor(textColor)
        if (action != null) {
            setAction(actionText) {
                dismiss()
                action()
            }
        }
        show()
    }
}

fun ViewGroup.showInfoSnackbar(
    message: String,
    actionText: Int = R.string.appbar_scrolling_view_behavior,
    length: Int = LENGTH_SHORT,
    action: (() -> Unit)? = null
) {
    showSnackbar(message, actionText, Color.WHITE, R.drawable.bg_snackbar_info, length, action)
}

fun ViewGroup.showErrorSnackbar(
    message: String,
    actionText: Int = R.string.ok,
    action: () -> Unit = {}
) {
    showSnackbar(message, actionText, Color.WHITE, R.drawable.bg_snackbar_error, LENGTH_INDEFINITE, action)
}