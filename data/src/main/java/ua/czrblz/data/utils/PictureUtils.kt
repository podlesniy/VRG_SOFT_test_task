package ua.czrblz.data.utils

import android.widget.ImageView
import com.bumptech.glide.Glide

fun loadPictureInLargeFormat(imageView: ImageView, imageUrl: String) {
    Glide.with(imageView.context)
        .load(imageUrl)
        .into(imageView)
}

fun loadPictureThumbnail(imageView: ImageView, imageUrl: String) {
    Glide.with(imageView.context)
        .load(imageUrl)
        .centerCrop()
        .into(imageView)
}