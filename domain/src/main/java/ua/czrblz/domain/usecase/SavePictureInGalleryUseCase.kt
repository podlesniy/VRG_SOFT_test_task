package ua.czrblz.domain.usecase

import android.app.Application
import ua.czrblz.domain.repository.PictureRepository

class SavePictureInGalleryUseCase(
    private val applicationContext: Application,
    private val pictureRepository: PictureRepository
) {

    fun execute(imageUrl: String,  onComplete: (String) -> Unit, onError: () -> Unit) {
        pictureRepository.savePictureToStorage(imageUrl, applicationContext, onComplete, onError)
    }
}