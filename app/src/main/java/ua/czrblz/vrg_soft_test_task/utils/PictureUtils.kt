package ua.czrblz.vrg_soft_test_task.utils

import android.widget.ImageView
import com.bumptech.glide.Glide

val listOfImageExtensions = listOf("jpg", "jpeg", "png", "bmp", "gif", "webp")

fun String.isInListOfImageExtensions() = this.split(".").last() in listOfImageExtensions

fun ImageView.loadPictureInLargeFormat(imageUrl: String) {
    Glide.with(this)
        .load(imageUrl)
        .into(this)
}

fun ImageView.loadPictureThumbnail(imageUrl: String) {
    Glide.with(this)
        .load(imageUrl)
        .centerCrop()
        .into(this)
}