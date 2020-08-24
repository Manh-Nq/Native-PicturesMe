package com.tapi.picturesme.core.server

import com.google.gson.GsonBuilder
import com.tapi.picturesme.PhotoItem
import okhttp3.OkHttpClient
import okhttp3.ResponseBody
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.http.Url
import java.util.concurrent.TimeUnit

private const val BASE_URL = "https://api.unsplash.com/"
private const val CLIENT_ID = "Dq7t7v4s6jR-5hwHV1r9v8wmhlaY-NIi4zlbriJTH44"



val client = OkHttpClient.Builder()
    .callTimeout(60, TimeUnit.SECONDS)
    .hostnameVerifier({ s, sslSession -> true }).build()


private val retrofit = Retrofit.Builder()
    .addConverterFactory(GsonConverterFactory.create())
    .client(client)
    .baseUrl(BASE_URL)
    .build()


/**
 * A public Api object that exposes the lazy-initialized Retrofit service
 */

//khoi tao api
object ApiService {
    val retrofitService: IPhotoService by lazy { retrofit.create(IPhotoService::class.java) }
}

/**
 * A public interface that exposes the [getProperties] method
 */

//tao interface de call API
interface IPhotoService {
    @GET("/photos/")
    suspend fun getPictures(
        @Query("page") page: Int = 1,
        @Query("per_page") perPage: Int = 20,
        @Query("client_id") clientID: String = CLIENT_ID
    ): List<PhotoItem>

    @GET()
    suspend fun getPhotoFromSever(@Url url: String): ResponseBody
}
