package com.tapi.picturesme.core.database.entity

import android.os.Parcel
import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "photo")
class PhotoEntity() : Parcelable {
    @PrimaryKey
    @ColumnInfo(name = "path")
    var path: String = ""

    @ColumnInfo(name = "isDownload")
    var isDownload: Boolean = true

    constructor(parcel: Parcel) : this() {
        path = parcel.readString()!!
        isDownload = parcel.readByte() != 0.toByte()
    }


    override fun toString(): String {
        return "PhotoEntity(path='$path', isDownload=$isDownload)"
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(path)
        parcel.writeByte(if (isDownload) 1 else 0)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<PhotoEntity> {
        override fun createFromParcel(parcel: Parcel): PhotoEntity {
            return PhotoEntity(parcel)
        }

        override fun newArray(size: Int): Array<PhotoEntity?> {
            return arrayOfNulls(size)
        }
    }


}
