package com.tapi.picturesme.core.database

import android.util.Log
import java.io.RandomAccessFile
import java.net.URL
import java.net.URLConnection

class DownLoadPhoto() {
    val DONE = "DONE"
    val FAIL = "FAIL"
    var type: String = ""
    suspend fun downLoadImage(link: String, path: String): String {
        try {
            var raf: RandomAccessFile = RandomAccessFile(path, "rw")
            var url: URLConnection = URL(link).openConnection()
            var inputs = url.getInputStream()

            var buff = ByteArray(1024)

            var length = inputs.read(buff)
            while (length > 0) {
                raf.write(buff, 0, length)
                length = inputs.read(buff)
            }
            type = DONE
            raf.close()
            inputs.close()
        } catch (e: Exception) {
            type = FAIL
            Log.d("TAG", "downLoadImage: ${e.printStackTrace()}")
        }
        return type

    }
}