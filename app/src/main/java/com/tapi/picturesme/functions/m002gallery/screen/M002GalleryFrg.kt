package com.tapi.picturesme.functions.m002gallery.screen

import android.util.Log
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tapi.picturesme.R
import com.tapi.picturesme.core.database.entity.PhotoEntity
import com.tapi.picturesme.functions.m001home.PhotoItemView
import com.tapi.picturesme.functions.m001home.screen.M001HomeFrg
import com.tapi.picturesme.functions.m002gallery.adapter.GalleryAdapter
import com.tapi.picturesme.functions.m003detail.screen.M003DetailFrg
import com.tapi.picturesme.view.base.BaseFragment

class M002GalleryFrg : BaseFragment(), GalleryAdapter.clickItemListener {
    val TAG = M002GalleryFrg::class.java.name
    lateinit var rvGallery: RecyclerView
    lateinit var galleryAdapter: GalleryAdapter
    lateinit var viewModel: GalleryViewModel

    override fun initViews() {
        viewModel = ViewModelProvider(this).get(GalleryViewModel::class.java)
        rvGallery = findViewById(R.id.rv_gallery, this)
        initData()
        observeViewModel()
    }

    private fun initData() {
        rvGallery.layoutManager = GridLayoutManager(mContext, 2)

        galleryAdapter = GalleryAdapter(mContext)
        galleryAdapter.setClickItemListener(this)
        rvGallery.adapter = galleryAdapter
    }

    private fun observeViewModel() {
        viewModel.getListPhoto().observe(this, Observer {
            var listData = it
            Log.d(TAG, "observeViewModel: ${listData.size}")
            galleryAdapter.submitList(it)

        })
    }

    override fun getLayoutByID(): Int {
        return R.layout.m002_gallery_frg
    }

    override fun showPreviousFrg() {
        mCallback.showFragment(M001HomeFrg().TAG)
    }

    override fun showDetail(item: PhotoEntity) {
        getStorage().photoItem = item
        mCallback.showFragment(M003DetailFrg().TAG)

    }


}