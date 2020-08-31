package com.tapi.picturesme.view.base

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.tapi.picturesme.App
import com.tapi.picturesme.utils.StorageCommon
import com.tapi.picturesme.view.event.OnActionCallBack

abstract class BaseFragment : Fragment(), View.OnClickListener {
    lateinit var mContext: Context
    lateinit var mView: View
    lateinit var mCallback: OnActionCallBack

    fun setOnCallBack(event: OnActionCallBack) {
        mCallback = event
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mContext = context
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mView = inflater.inflate(getLayoutByID(), container, false)
        initViews()
        return mView
    }


    abstract fun initViews()

    abstract fun getLayoutByID(): Int


    fun <T : View> findViewById(id: Int, event: View.OnClickListener): T {
        val view: View = mView.findViewById(id)
        if (view != null && event != null) {
            view.setOnClickListener(event)
        }
        return view as T
    }

    fun <T : View> findViewById(id: Int): T {
        val view: View = mView.findViewById(id)

        return view as T
    }

    open fun showToast(text: String) {
        if (text != null) {
            Toast.makeText(mContext, text, Toast.LENGTH_SHORT).show()
        }
    }

    abstract fun showPreviousFrg()

    override fun onClick(p0: View) {
        //do no thing
    }

    fun textOf(edt: EditText): String {
        return edt.text.toString().trim()
    }

    fun textOf(tv: TextView): String {
        return tv.text.toString().trim()
    }

   open fun getStorage(): StorageCommon {
        return App.storageCommon
    }

}


