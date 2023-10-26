package com.example.nework.view

import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation
import com.bumptech.glide.load.resource.bitmap.CircleCrop

fun ImageView.load(url: String, errorPlaceholder: Int, vararg transforms: BitmapTransformation = emptyArray()) =
    Glide.with(this)
        .load(url)
        .error(errorPlaceholder)
        .timeout(10_000)
        .transform(*transforms)
        .into(this)

fun ImageView.loadCircleCrop(url: String, error: Int, vararg transforms: BitmapTransformation = emptyArray()) =
    load(url, error, CircleCrop(), *transforms)
