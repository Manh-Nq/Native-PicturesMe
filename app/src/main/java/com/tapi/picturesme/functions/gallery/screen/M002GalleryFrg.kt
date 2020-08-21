package com.tapi.picturesme.functions.gallery.screen

import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import com.bumptech.glide.Glide
import com.tapi.picturesme.R
import com.tapi.picturesme.functions.home.PhotoItemView
import com.tapi.picturesme.functions.home.screen.M001HomeFrg
import com.tapi.picturesme.view.base.BaseFragment
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class M002GalleryFrg : BaseFragment() {
    val TAG = M002GalleryFrg::class.java.name
    lateinit var ivImage: ImageView
    lateinit var progress: ProgressBar


    override fun initViews() {

//        progress = findViewById(R.id.progress, this)
//        progress.visibility = View.VISIBLE
//        ivImage = findViewById(R.id.iv_image_002, this)
//        var photoItem: PhotoItemView = getStorage().photoItem
//        GlobalScope.launch(Dispatchers.Main) {
//            Glide.with(mContext).load(photoItem.photoItem.picture.thumb).into(ivImage)
//            progress.visibility = View.GONE
//        }

    }


    override fun getLayoutByID(): Int {
        return R.layout.frg_2
    }

    override fun showPreviousFrg() {
        mCallback.showFragment(M001HomeFrg().TAG)
    }

}