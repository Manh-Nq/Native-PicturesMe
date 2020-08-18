package com.tapi.picturesme.view.base

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModel
import com.tapi.picturesme.App
import com.tapi.picturesme.R
import com.tapi.picturesme.utils.StorageCommon
import com.tapi.picturesme.view.event.OnActionCallBack

abstract class BaseActivity<T : ViewModel> : AppCompatActivity(), OnActionCallBack {
    open lateinit var mViewModel: T
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(getLayoutByID())
        mViewModel = getViewModel() as T
        registerViewModel()
        initViews()
    }

    abstract fun registerViewModel()

    abstract fun getViewModel(): Any

    abstract fun initViews()

    abstract fun getLayoutByID(): Int

    open fun getStorage(): StorageCommon {
        return App.storageCommon
    }

     fun <T : View?> findViewById(id: Int, event:View.OnClickListener): T {
         var v:View= findViewById(id)
         if(v!=null && event!=null){
             v.setOnClickListener(event)
         }
        return v as T
    }

    open fun showToast(text: String) {
        if (text != null) {
            Toast.makeText(this, text, Toast.LENGTH_SHORT).show()

        }
    }

    override fun showFragment(tag: String) {

        try {
            var fragment =
                supportFragmentManager.findFragmentByTag(tag) as BaseFragment?
            if (fragment == null) {
                val clazz = Class.forName(tag)
                val cons = clazz.getConstructor()
                fragment = cons.newInstance() as BaseFragment
            }
            fragment.setOnCallBack(this)
            supportFragmentManager
                .beginTransaction()
                .replace(R.id.ln_main, fragment!!, tag)
                .commit()
            getStorage().currentTag = tag
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

}