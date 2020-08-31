package com.tapi.picturesme.core.database.entity

import android.os.Parcel
import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "photo")
class PhotoEntity() {
    @PrimaryKey
    @ColumnInfo(name = "path")
    var path: String = ""

    @ColumnInfo(name = "isDownload")
    var isDownload: Boolean = false

}
