package com.vivek.wo.mvvm.sample

import android.databinding.BindingAdapter
import android.widget.ImageView

object BindingAdapters {
    @JvmStatic
    @BindingAdapter("imageUrl")
    fun setImageUrl(imageView: ImageView, url: String) {
        println("----------------- $url")
        imageView.setImageResource(R.drawable.ic_launcher_background)
    }
}
