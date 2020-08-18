package com.tapi.picturesme.view.activity


import android.view.View
import androidx.lifecycle.ViewModelProvider
import com.tapi.picturesme.R
import com.tapi.picturesme.function.HomeViewModel
import com.tapi.picturesme.view.base.BaseActivity
import com.tapi.picturesme.view.base.BaseFragment
import com.tapi.picturesme.view.fragment.M001Frg

class HomeActivity : BaseActivity<HomeViewModel>(), View.OnClickListener {

    var listData: List<String> = listOf()


    override fun getViewModel(): Any {
        return ViewModelProvider(this).get(HomeViewModel().TAG)
    }

    override fun initViews() {
        showFragment(M001Frg().TAG)

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