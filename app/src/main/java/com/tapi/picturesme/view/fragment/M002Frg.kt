package com.tapi.picturesme.view.fragment

import android.widget.TextView
import com.tapi.picturesme.R
import com.tapi.picturesme.view.base.BaseFragment

class M002Frg : BaseFragment() {
    val TAG = M002Frg::class.java.name


    override fun initViews() {
    }

    override fun getLayoutByID(): Int {
        return R.layout.frg_2
    }

    override fun showPreviousFrg() {
        mCallback.showFragment(M001Frg().TAG)
    }

}