package com.tapi.picturesme.view.fragment

import com.tapi.picturesme.R
import com.tapi.picturesme.functions.m001home.screen.M001HomeFrg
import com.tapi.picturesme.utils.CommonUtils
import com.tapi.picturesme.view.base.BaseFragment
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


class M000Splash : BaseFragment() {
   val TAG = M000Splash::class.java.name
    override fun initViews() {

        CommonUtils.myCoroutineScope.launch {
            delay(2000)
            mCallback.showFragment(M001HomeFrg().TAG, true)

        }

//        Handler().postDelayed(Runnable {
//            mCallback.showFragment(M001HomeFrg().TAG)
//        }, 2000)
    }

    override fun getLayoutByID(): Int {
        return R.layout.frg_splash
    }

    override fun showPreviousFrg() {
        throw NullPointerException()
    }




}