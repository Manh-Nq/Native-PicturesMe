package com.tapi.picturesme.view.fragment

import android.os.Handler
import com.tapi.picturesme.R
import com.tapi.picturesme.view.base.BaseFragment

class M000Splash : BaseFragment() {
    val TAG = M000Splash::class.java.name
    override fun initViews() {
        Handler().postDelayed(Runnable {
            mCallback.showFragment(M001HomeFrg().TAG)
        }, 2000)
    }

    override fun getLayoutByID(): Int {
        return R.layout.frg_splash
    }

    override fun showPreviousFrg() {
        throw NullPointerException()
    }

}