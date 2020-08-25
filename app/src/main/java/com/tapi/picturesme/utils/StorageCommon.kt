package com.tapi.picturesme.utils

import com.tapi.picturesme.core.database.entity.PhotoEntity
import com.tapi.picturesme.functions.m001home.PhotoItemView

object StorageCommon {
    var currentTag: String = ""
    var page: Int = 0
    lateinit var photoItem: PhotoEntity
}