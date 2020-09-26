package com.example.googlebooksapi

import android.graphics.drawable.Drawable
import android.view.View.GONE
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestBuilder
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target

fun ImageView.load(url: String) {
    Glide.with(this).load(url).diskCacheStrategy(DiskCacheStrategy.DATA)
        .transform(CenterCrop(), RoundedCorners(5)).transition(withCrossFade(500))
        .onFailure { this.visibility = GONE }.into(this)
}

inline fun RequestBuilder<Drawable>.onFailure(crossinline action: () -> Unit) =
    addListener(object : RequestListener<Drawable?> {
        override fun onLoadFailed(
            e: GlideException?,
            model: Any?,
            target: Target<Drawable?>?,
            isFirstResource: Boolean
        ): Boolean {
            action()
            return false
        }

        override fun onResourceReady(
            resource: Drawable?,
            model: Any?,
            target: Target<Drawable?>?,
            dataSource: DataSource?,
            isFirstResource: Boolean
        ): Boolean = false

    })