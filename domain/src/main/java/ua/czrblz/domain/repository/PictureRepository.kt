package ua.czrblz.domain.repository

import android.content.Context
import android.widget.ImageView

interface PictureRepository {
    fun openPictureInLargeFormat(imageView: ImageView, imageUrl: String)
    suspend fun savePictureToStorage(imageUrl: String, context: Context): Boolean
}