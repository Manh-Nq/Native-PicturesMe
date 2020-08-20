package com.tapi.picturesme.utils

import com.tapi.picturesme.PhotoItem
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

private const val BASE_URL = "https://api.unsplash.com/"
private const val CLIENT_ID = "Dq7t7v4s6jR-5hwHV1r9v8wmhlaY-NIi4zlbriJTH44"

/**
 * Use the Retrofit builder to build a retrofit object using a Moshi converter with our Moshi
 * object.
 */
private val retrofit = Retrofit.Builder()
    .addConverterFactory(GsonConverterFactory.create())
    .baseUrl(BASE_URL)
    .build()

/**
 * A public interface that exposes the [getProperties] method
 */
interface IPhotoService {
    @GET("/photos/")
    suspend fun getPictures(
        @Query("page") page: Int = 1,
        @Query("per_page") perPage: Int = 10,
        @Query("client_id") clientID: String = CLIENT_ID
    ): List<PhotoItem>
}

/**
 * A public Api object that exposes the lazy-initialized Retrofit service
 */
object getRetrofit {
    val retrofitService: IPhotoService by lazy { retrofit.create(IPhotoService::class.java) }
}
