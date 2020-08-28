package com.tapi.picturesme

import android.app.Application
import androidx.room.Room
import com.tapi.picturesme.core.database.dao.PhotoDatabase
import com.tapi.picturesme.functions.m002gallery.screen.M002GalleryFrg
import com.tapi.picturesme.functions.m003detail.screen.M003DetailFrg
//import com.tapi.picturesme.core.database.dao.PhotoDatabase
import com.tapi.picturesme.utils.StorageCommon

class App : Application() {
    companion object {
        lateinit var instance: App
        lateinit var storageCommon: StorageCommon
        lateinit var photoDatabase: PhotoDatabase
        lateinit var m002GalleryFrg: M002GalleryFrg
        lateinit var m003DetailFrg: M003DetailFrg

    }

    override fun onCreate() {
        super.onCreate()
        storageCommon = StorageCommon
        instance = this
        m002GalleryFrg= M002GalleryFrg()
        m003DetailFrg= M003DetailFrg()
        photoDatabase = Room.databaseBuilder(this, PhotoDatabase::class.java, "photo").build()
    }
}