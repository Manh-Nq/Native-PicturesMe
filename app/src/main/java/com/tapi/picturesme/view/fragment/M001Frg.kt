package com.tapi.picturesme.view.fragment

import android.view.View
import android.widget.Button
import com.tapi.picturesme.R
import com.tapi.picturesme.view.base.BaseFragment
import java.lang.NullPointerException

class M001Frg : BaseFragment() {
    val TAG = M001Frg::class.java.name


    override fun initViews() {
        findViewById<Button>(R.id.bt_next, this)
    }

    override fun getLayoutByID(): Int {
        return R.layout.frg_1
    }

    override fun showPreviousFrg() {
        throw NullPointerException()
    }

    override fun onClick(p0: View) {
        when(p0.id){
            R.id.bt_next->nextFrg()
        }
    }

    private fun nextFrg() {
        mCallback.showFragment(M002Frg().TAG)
    }


}