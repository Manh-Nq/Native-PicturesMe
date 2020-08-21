package com.tapi.picturesme

import android.app.Application
import androidx.room.Room
import com.tapi.picturesme.core.database.database.PhotoDatabase
//import com.tapi.picturesme.core.database.dao.PhotoDatabase
import com.tapi.picturesme.utils.StorageCommon

class App : Application() {
    companion object {
        lateinit var instance: App
        lateinit var storageCommon: StorageCommon
        lateinit var photoDatabase: PhotoDatabase
    }

    override fun onCreate() {
        super.onCreate()
        storageCommon = StorageCommon
        instance = this
        photoDatabase= Room.databaseBuilder(this,PhotoDatabase::class.java,"photo").build()
    }
}