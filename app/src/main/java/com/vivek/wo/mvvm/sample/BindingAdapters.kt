package com.vivek.wo.mvvm.sample

import android.databinding.BindingAdapter
import android.widget.ImageView

object BindingAdapters {
    @BindingAdapter("app:remoteImageUrl")
    @JvmStatic
    fun setRemoteImageUrl(imageView: ImageView, url: String?) {
        println("----------------- $url")
        imageView.setImageResource(R.drawable.ic_launcher_background)
    }
}

