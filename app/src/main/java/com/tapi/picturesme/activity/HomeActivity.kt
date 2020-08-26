package com.tapi.picturesme.activity


import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.util.Log
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import com.tapi.picturesme.R
import com.tapi.picturesme.functions.m001home.screen.M001HomeFrg
import com.tapi.picturesme.utils.CommonUtils
import com.tapi.picturesme.view.base.BaseActivity
import com.tapi.picturesme.view.base.BaseFragment
import com.tapi.picturesme.view.fragment.M000Splash
import kotlinx.android.synthetic.main.home_activity.*

class HomeActivity : BaseActivity(), View.OnClickListener {

    val m001Frg: M001HomeFrg = M001HomeFrg()

    lateinit var tvNoti: TextView

    @RequiresApi(Build.VERSION_CODES.M)
    override fun initViews() {
        tvNoti = findViewById(R.id.tv_notification, this)

        checkPermissions()

        if (!CommonUtils.isNetworkConnected(this)) {
            tvNoti.visibility = View.VISIBLE
            iv_warning.visibility = View.VISIBLE
            Log.d("TAG", "delaytime: this is check network")
            showToast("thiết bị của bạn chưa được kết nối internet", Toast.LENGTH_LONG)
        } else {
            Log.d("TAG", "delaytime: this is check network done and show frg")
            showFragment(M000Splash().TAG)

        }

    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun checkPermissions() {
        Log.d("TAG", "initViewsACT: this is checkper")
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.READ_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            requestPermissions(
                arrayOf(
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.READ_EXTERNAL_STORAGE
                ), 100
            )
        }
    }


    override fun getLayoutByID(): Int {
        return R.layout.home_activity
    }

    override fun onBackPressed() {
        Log.d("TAG", "initViewsACT: this is onbackpress")
        var currentTag = getStorage().currentTag
        if (currentTag == null) {
            super.onBackPressed()
            return
        }

        val frg: BaseFragment? =
            supportFragmentManager.findFragmentByTag(currentTag) as BaseFragment?
        try {
            frg?.showPreviousFrg()
        } catch (e: Exception) {
            e.printStackTrace()
            Log.d("TAG", "onBackPressed:${e.printStackTrace()} ")
        }

    }


    override fun onClick(p0: View?) {

    }

//    open fun isNetworkConnected(): Boolean {
//        Log.d("TAG", "isnetworkConnected: check network connect")
//
//        val cm: ConnectivityManager =
//            this.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
//
//        Log.d("TAG", "isnetworkConnected: this is check network done")
//        return cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo()!!.isConnected()
//    }

}