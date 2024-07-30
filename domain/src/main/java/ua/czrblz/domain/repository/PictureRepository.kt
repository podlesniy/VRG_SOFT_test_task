package ua.czrblz.domain.repository

import android.content.Context

interface PictureRepository {
    fun savePictureToStorage(imageUrl: String, context: Context, onComplete: (String) -> Unit, onError: () -> Unit)
}