package com.tapi.picturesme.activity


import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.view.View
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.tapi.picturesme.R
import com.tapi.picturesme.functions.home.viewmodel.HomeViewModel
import com.tapi.picturesme.view.base.BaseActivity
import com.tapi.picturesme.view.base.BaseFragment
import com.tapi.picturesme.view.fragment.M000Splash

class HomeActivity : BaseActivity<HomeViewModel>(), View.OnClickListener {

    var listData: List<String> = listOf()


    override fun getViewModel(): Any {
        return ViewModelProvider(this).get(HomeViewModel::class.java)
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun initViews() {
       checkPermissions()
        showFragment(M000Splash().TAG)

    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun checkPermissions() {
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

    override fun registerViewModel() {

    }

    override fun onBackPressed() {
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
            super.onBackPressed()
        }

    }

    override fun onClick(p0: View?) {

    }



}