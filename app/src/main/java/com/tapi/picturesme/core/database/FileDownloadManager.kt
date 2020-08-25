package com.tapi.picturesme.core.database

import android.content.Context
import android.content.ContextWrapper
import android.graphics.Bitmap
import android.util.Log
import com.tapi.picturesme.App
import com.tapi.picturesme.PhotoItem
import com.tapi.picturesme.core.database.entity.PhotoEntity
import com.tapi.picturesme.utils.CommonUtils
import kotlinx.coroutines.Deferred
import java.io.File
import java.io.FileOutputStream
import java.io.IOException


class DownLoadPhoto() {


     fun saveToInternalStorage(bitmapImage: Bitmap, path: String): String? {

         val cw = ContextWrapper(App.instance.getApplicationContext())
         // path to /data/data/yourapp/app_data/imageDir
         val directory: File = cw.getDir("imageDir", Context.MODE_PRIVATE)
         // Create imageDir
         val mypath = File(directory, path)
         var out: FileOutputStream? = null
         try {
             out = FileOutputStream(mypath)
             // Use the compress method on the BitMap object to write image to the OutputStream
             bitmapImage.compress(Bitmap.CompressFormat.PNG, 100, out)
         } catch (e: Exception) {
             e.printStackTrace()
         } finally {
             try {
                 out?.close()
             } catch (e: IOException) {
                 e.printStackTrace()
             }
         }
        return directory.getAbsolutePath()
    }


    suspend fun isDownloaded(photoItem: PhotoItem): Boolean {
        var listDB = App.photoDatabase.photoDAO.getListPhoto()
        for (item in listDB) {
            val link = photoItem.picture.raw
            Log.d("TAG", "isDownloaded11: $link")
            val path = link.substring(link.indexOf('-') + 1, link.indexOf('?')) + ".png"
            Log.d("TAG", "isDownloaded: $path")
            var pathDb = item.path.substring(item.path.lastIndexOf('/') + 1, item.path.length)


            Log.d("TAG", "isDownloading: $pathDb")
            Log.d("TAG", "isDownloading: ${item.path}")

            if (pathDb.equals(path)) {
                return true
            }
        }
        return false
    }

    fun deleteFile(path: String): Boolean {
        var file = File(path)
        file.delete()
        if (file.delete()) {
            return true
        }
        return false
    }


}

