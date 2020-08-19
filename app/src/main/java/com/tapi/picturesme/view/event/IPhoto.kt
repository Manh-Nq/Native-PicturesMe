package com.tapi.picturesme.view.event

import com.tapi.picturesme.model.PhotoItem
import kotlinx.coroutines.Deferred
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

open interface IPhoto {

    @GET("/photos/")
    fun getPhotos(
        @Query("page") page: Int = 1,
        @Query("per_page") perPage: Int = 10,
        @Query("client_id") clientID: String = "ZCrQRuxnXBxzR_sl0WeHvj9nMEdw5y-ySr5wbWDp7Sw"
    ): Call<List<PhotoItem>>



    @GET("/photos/")
    suspend fun getPictures(
        @Query("page") page: Int = 1,
        @Query("per_page") perPage: Int = 10,
        @Query("client_id") clientID: String = "ZCrQRuxnXBxzR_sl0WeHvj9nMEdw5y-ySr5wbWDp7Sw"
    ):List<PhotoItem>

}