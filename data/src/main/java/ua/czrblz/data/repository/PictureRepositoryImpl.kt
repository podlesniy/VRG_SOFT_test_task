package ua.czrblz.data.repository

import android.content.Context
import android.widget.ImageView
import ua.czrblz.domain.repository.PictureRepository

class PictureRepositoryImpl: PictureRepository {

    override fun openPictureInLargeFormat(imageView: ImageView, imageUrl: String) {
        TODO("Not yet implemented")
    }

    override suspend fun savePictureToStorage(imageUrl: String, context: Context): Boolean {
        TODO("Not yet implemented")
    }
}