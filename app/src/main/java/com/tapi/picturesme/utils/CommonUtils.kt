package com.tapi.picturesme.utils

import android.net.ConnectivityManager
import android.util.Log
import androidx.core.content.ContextCompat.getSystemService
import com.tapi.picturesme.App
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob

object CommonUtils {
    var job = SupervisorJob()
    var myCoroutineScope = CoroutineScope(Dispatchers.Main + job)

}