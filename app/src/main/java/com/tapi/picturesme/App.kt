package com.tapi.picturesme

import android.app.Application
import com.tapi.picturesme.utils.StorageCommon

class App : Application() {
    companion object {
        lateinit var instance: App
        lateinit var storageCommon: StorageCommon
    }

    override fun onCreate() {
        super.onCreate()
        storageCommon = StorageCommon
        instance = this
    }
}