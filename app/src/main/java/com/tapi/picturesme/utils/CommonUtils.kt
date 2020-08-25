package com.tapi.picturesme.utils

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.location.LocationManager
import android.net.ConnectivityManager
import android.provider.Settings
import com.tapi.picturesme.App
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob

object CommonUtils {
    var job = SupervisorJob()
    var myCoroutineScope = CoroutineScope(Dispatchers.Main + job)


    fun isNetworkConnected(activity: Activity): Boolean {
        val cm: ConnectivityManager =
            activity?.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        return cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo()!!.isConnected()
    }
}