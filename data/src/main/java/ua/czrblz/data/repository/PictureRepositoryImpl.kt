package ua.czrblz.data.repository

import android.content.ContentValues
import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import ua.czrblz.domain.repository.PictureRepository
import java.io.File

class PictureRepositoryImpl: PictureRepository {

    override fun savePictureToStorage(imageUrl: String, context: Context, onComplete: (String) -> Unit, onError: () -> Unit) {
        Glide.with(context)
            .asBitmap()
            .load(imageUrl)
            .into(object : CustomTarget<Bitmap>() {
                override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                    val pictureName = imageUrl.split("/").last()
                    saveBitmapToPictures(context, resource, pictureName, {
                        onComplete.invoke(it)
                    }) {
                        onError.invoke()
                        Log.e("Download_error", "Error downloading")
                    }
                }

                override fun onLoadCleared(placeholder: Drawable?) {}
            })
    }

    private fun saveBitmapToPictures(context: Context, bitmap: Bitmap?, imageName: String, onComplete: (String) -> Unit, onError: () -> Unit) {
        try {
            val imageMimeType = imageName.split(".").last()
            val compressFormat = Bitmap.CompressFormat.entries.firstOrNull { it.name == imageMimeType }
            val contentValues = ContentValues().apply {
                put(MediaStore.Images.Media.DISPLAY_NAME, imageName)
                put(MediaStore.Images.Media.MIME_TYPE, "image/$imageMimeType")
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    put(MediaStore.Images.Media.RELATIVE_PATH, "Pictures")
                } else {
                    put(MediaStore.Images.Media.DATA, File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), imageName).absolutePath)
                }
            }

            val resolver = context.contentResolver
            val uri = resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)

            uri?.let {
                resolver.openOutputStream(it)?.use { outputStream ->
                    bitmap?.compress(compressFormat ?: Bitmap.CompressFormat.JPEG, 100, outputStream)
                    outputStream.flush()
                    outputStream.close()
                    return@let onComplete.invoke(imageName)
                }
            }
        } catch (e: Exception) {
            Log.e("SavePictureError", "Error saving picture", e)
            onError.invoke()
        }
    }
}