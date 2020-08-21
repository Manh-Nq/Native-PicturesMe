package com.tapi.picturesme.core

import android.R
import android.content.Context
import android.content.ContextWrapper
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import android.widget.ImageView
import com.tapi.picturesme.App
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.RandomAccessFile
import java.net.URL
import java.net.URLConnection


class DownLoadPhoto() {


    suspend fun saveToInternalStorage(bitmapImage: Bitmap, path: String): String? {

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
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        } finally {
            try {
                out?.close()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        return directory.getAbsolutePath()
    }

    private fun loadImageFromStorage(path: String) {
        try {
            val f = File(path, "profile.jpg")
            val b = BitmapFactory.decodeStream(FileInputStream(f))
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }}