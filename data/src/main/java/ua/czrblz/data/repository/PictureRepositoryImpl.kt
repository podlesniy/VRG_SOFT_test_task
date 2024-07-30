package ua.czrblz.data.repository

import android.content.Context
import android.os.Environment
import android.util.Log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import ua.czrblz.domain.repository.PictureRepository
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

class PictureRepositoryImpl: PictureRepository {

    override fun savePictureToStorage(imageUrl: String, context: Context, onComplete: (String) -> Unit, onError: () -> Unit) {
        CoroutineScope(Dispatchers.IO).launch {
            val client = OkHttpClient()
            val request = Request.Builder()
                .url(imageUrl)
                .build()
            val pictureName = imageUrl.split("/").last()
            try {
                val response = client.newCall(request).execute()
                if (response.isSuccessful) {

                    val body = response.body()
                    val file = File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), pictureName)
                    val output = FileOutputStream(file)
                    val input = body?.byteStream()
                    val data = ByteArray(1024)
                    var count: Int

                    while (input?.read(data).also { count = it!! } != -1) {
                        output.write(data, 0, count)
                    }
                    output.flush()
                    output.close()
                    input?.close()
                    withContext(Dispatchers.Main) {
                        onComplete.invoke(pictureName)
                    }
                } else {
                    withContext(Dispatchers.Main) {
                        onError.invoke()
                    }
                    Log.e("Download_error", "HTTP request failed with code: ${response.code()}")
                }
            } catch (e: IOException) {
                withContext(Dispatchers.Main) {
                    onError.invoke()
                }
                Log.e("Download_error", "Error downloading ${e.message}")
            }
        }
    }
}