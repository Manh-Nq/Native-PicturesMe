package com.tapi.picturesme.core.database.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.tapi.picturesme.core.database.dao.PhotoDAO
import com.tapi.picturesme.core.database.entity.PhotoEntity

@Database(entities = [PhotoEntity::class], version = 1)
abstract class PhotoDatabase : RoomDatabase() {
    abstract val photoDAO: PhotoDAO
}