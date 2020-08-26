package com.tapi.picturesme.core.server

import com.tapi.picturesme.PhotoItem


class APIHelper {
    suspend fun getListData(): List<PhotoItem>? {
        try {
            return ApiService.retrofitService.getPictures(1)
        } catch (e: Exception) {
            return null
        }

    }
}