package com.tapi.picturesme.functions.m003detail.screen

import android.view.View
import android.widget.ImageView
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.tapi.picturesme.R
import com.tapi.picturesme.core.database.entity.PhotoEntity
import com.tapi.picturesme.functions.m002gallery.screen.M002GalleryFrg
import com.tapi.picturesme.view.base.BaseFragment


class M003DetailFrg : BaseFragment() {
    val TAG = M003DetailFrg::class.java.name
    lateinit var ivImage: ImageView
    lateinit var ivDelete: ImageView
    lateinit var item: PhotoEntity
    lateinit var viewModel: DetailViewmodel

    override fun initViews() {
        viewModel = ViewModelProvider(this).get(DetailViewmodel::class.java)
        item = getStorage().photoItem
        ivImage = findViewById(R.id.iv_detail, this)
        ivDelete = findViewById(R.id.iv_delete, this)

        obseverViewmodel()
    }

    private fun obseverViewmodel() {
        viewModel.getImage().observe(this, Observer {
            val requestOptions = RequestOptions()
                .diskCacheStrategy(DiskCacheStrategy.NONE) // because file name is always same
                .skipMemoryCache(true)
                .centerInside()

            Glide.with(mContext).load(item.path).centerCrop().apply(requestOptions).centerCrop()
                .into(
                    ivImage
                )
        })
    }

    override fun onClick(p0: View) {
        when (p0.id) {
            R.id.iv_delete -> showDialogDelete()
        }
    }

    private fun showDialogDelete() {
        val dialog: AlertDialog = AlertDialog.Builder(mContext).create()
        dialog.setTitle("Delete")
        dialog.setMessage("you want to delete this picture")
        dialog.setButton(android.app.AlertDialog.BUTTON_POSITIVE, "OK")
        { dialog, which -> deleteFromDatabase() }
        dialog.setButton(android.app.AlertDialog.BUTTON_NEGATIVE, "Cancel")
        { dialog, which -> dialog.dismiss() }
        dialog.show()
    }

    private fun deleteFromDatabase() {
        viewModel.deteleImage(item)
        mCallback.showFragment(M002GalleryFrg().TAG)
    }

    override fun getLayoutByID(): Int {
        return R.layout.m003_detail_frg
    }

    override fun showPreviousFrg() {
        mCallback.showFragment(M002GalleryFrg().TAG)
    }

}