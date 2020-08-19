package com.tapi.picturesme.core.server

import android.util.Log
import com.tapi.picturesme.model.PhotoItem
import com.tapi.picturesme.utils.CommonUtils
import com.tapi.picturesme.view.event.IPhoto
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

object APIHelper {
    val END_POINT = "https://api.unsplash.com/"
    val CLIENT_ID = "Dq7t7v4s6jR-5hwHV1r9v8wmhlaY-NIi4zlbriJTH44"
    var page = 1
    var per_page = 10

    suspend fun getDataFromSever(): List<PhotoItem> {

        var api: IPhoto = CommonUtils.getRetrofit(END_POINT).create(IPhoto::class.java)
        api.getPhotos(page, per_page, CLIENT_ID)
            .enqueue(object : Callback<List<PhotoItem>> {
                override fun onResponse(
                    call: Call<List<PhotoItem>>,
                    response: Response<List<PhotoItem>>
                ) {
                    Log.d("TAG", "onResponse: ${response.body()}")
                }

                override fun onFailure(call: Call<List<PhotoItem>>, t: Throwable) {
                    Log.d("TAG", "onFailure: ${t.message}")
                }

            })
        return listOf()
    }

}