package com.tapi.picturesme.utils

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.net.ssl.HostnameVerifier
import kotlin.system.measureTimeMillis

object CommonUtils {
    var job = Job()
    var myCoroutineScope = CoroutineScope(Dispatchers.Default + job)

    fun getRetrofit(endpoint: String): Retrofit {
        val client: OkHttpClient =
            OkHttpClient.Builder()
                .callTimeout(30, TimeUnit.SECONDS)
                .hostnameVerifier(
                    { hostname, session -> true })
                .build()
        var retrofit: Retrofit =
            Retrofit.Builder()
                .baseUrl(endpoint)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        return retrofit
    }

}