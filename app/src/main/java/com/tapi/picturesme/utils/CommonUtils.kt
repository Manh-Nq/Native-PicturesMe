package com.tapi.picturesme.utils

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.net.ssl.HostnameVerifier
import kotlin.system.measureTimeMillis

object CommonUtils {
    var job = SupervisorJob()
    var myCoroutineScope = CoroutineScope(Dispatchers.Main + job)

}