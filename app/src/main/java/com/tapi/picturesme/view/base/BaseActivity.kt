package com.tapi.picturesme.view.base

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.tapi.picturesme.App
import com.tapi.picturesme.R
import com.tapi.picturesme.utils.StorageCommon
import com.tapi.picturesme.view.event.OnActionCallBack

abstract class BaseActivity : AppCompatActivity(), OnActionCallBack {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(getLayoutByID())
        initViews()
    }


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
    open fun showToast(text: String, duration:Int) {
        if (text != null) {
            Toast.makeText(this, text, duration).show()
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
                .beginTransaction().replace(R.id.ln_main, fragment, tag)
                .commit()
            getStorage().currentTag = tag
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

}