package com.tapi.picturesme.functions.home.screen

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.tapi.picturesme.R
import com.tapi.picturesme.functions.gallery.screen.M002GalleryFrg
import com.tapi.picturesme.functions.home.PhotoItemView
import com.tapi.picturesme.functions.home.adapter.PhotoAdapter
import com.tapi.picturesme.utils.ApiService
import com.tapi.picturesme.utils.CommonUtils
import com.tapi.picturesme.view.base.BaseFragment
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class M001HomeFrg : BaseFragment(), PhotoAdapter.adapterListener {
    val TAG = M001HomeFrg::class.java.name
    lateinit var rvPhoto: RecyclerView
    lateinit var photoAdapter: PhotoAdapter
    lateinit var homeViewModel: HomeViewModel
    lateinit var btAlbum: FloatingActionButton
    lateinit var progressBarLoading: ProgressBar

    override fun initViews() {
        progressBarLoading = findViewById(R.id.progress_loadmore, this)
        btAlbum = findViewById(R.id.bt_album, this)
        homeViewModel = ViewModelProvider(this).get(HomeViewModel().TAG)
        rvPhoto = findViewById(R.id.rv_photo, this)


        initList()
        observeViewModel()

        recycleListener()
    }

    private fun initList() {
        rvPhoto.layoutManager = GridLayoutManager(mContext, 2)

        photoAdapter = PhotoAdapter(mContext)
        photoAdapter.setCallBackAdapterHome(this)
        rvPhoto.adapter = photoAdapter
    }

    private fun observeViewModel() {
        homeViewModel.getListData().observe(this, Observer {
            photoAdapter.submitList(it)
        })
        homeViewModel.getIsloading().observe(this, Observer {
            progressBarLoading.visibility = if (it) View.VISIBLE else View.GONE
        })
    }

    private fun recycleListener() {
        rvPhoto.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                if (dy > 0) {
                    val gridLayoutManager: GridLayoutManager =
                        rvPhoto.layoutManager as GridLayoutManager

                    val visibleItemCount = gridLayoutManager.childCount
                    val passVisibleItem = gridLayoutManager.findFirstCompletelyVisibleItemPosition()
                    val total = photoAdapter.itemCount

                    if (!homeViewModel.loading.value!!) {

                        if ((visibleItemCount + passVisibleItem) > total) {
                            homeViewModel.loadMore()
                        }

                    }
                }
            }
        })
    }


    override fun onClick(p0: View) {
        when (p0.id) {
            R.id.bt_album -> toMyAlbum()
        }
    }

    private fun toMyAlbum() {
        mCallback.showFragment(M002GalleryFrg().TAG)
    }

    // TODO: 19-Aug-20


    override fun getLayoutByID(): Int {
        return R.layout.frg_1
    }

    override fun showPreviousFrg() {
        throw NullPointerException()
    }

    override fun downLoad(
        item: PhotoItemView,
        progress: ProgressBar,
        viewBg: View,
        ivCircle: ImageView,
        ivDownload: ImageView,
        tvDownload: TextView
    ) {
        try {
            CommonUtils.myCoroutineScope.launch {
                withContext(Dispatchers.IO) {
                    var response =
                        ApiService.retrofitService.getPhotoFromSever(item.photoItem.picture.raw)
                    var bitMap: Bitmap = BitmapFactory.decodeStream(response.byteStream())

                }
                showToast("download success")
                progress.visibility = View.GONE
                viewBg.visibility = View.GONE

            }
        } catch (e: Exception) {
            e.printStackTrace()
            progress.visibility = View.GONE
            ivCircle.visibility = View.VISIBLE
            ivDownload.visibility = View.VISIBLE
            tvDownload.visibility = View.VISIBLE
        }

    }


//    override fun downLoad(
//        item: PhotoItemView,
//        progress: ProgressBar,
//        viewBg: View,
//        ivCircle: ImageView,
//        ivDownload: ImageView
//    ) {
//
//
//        val path =
//            Environment.getDataDirectory().toString() + "/data/" + App.instance.packageName
//        var link = item.photoItem.picture.raw
//
//        val fileName = path + "/${link.substring(link.indexOf('-') + 1, link.indexOf('?'))}"
//        CommonUtils.myCoroutineScope.launch {
//            var result =
//                async { DownLoadPhoto().downLoadImage(item.photoItem.picture.raw, fileName) }
//
//            Log.d(TAG, "downLoad: $fileName")
//
//            if (result.await().equals(DownLoadPhoto().DONE)) {
//                withContext(Dispatchers.Main) {
//                    Toast.makeText(mContext, "download success", Toast.LENGTH_SHORT).show()
//                    progress.visibility = View.GONE
//                    viewBg.visibility = View.GONE
//                }
//            } else if (result.await().equals(DownLoadPhoto().FAIL)) {
//                withContext(Dispatchers.Main) {
//                    Toast.makeText(mContext, "download fail", Toast.LENGTH_SHORT).show()
//                    progress.visibility = View.GONE
//                    ivCircle.visibility = View.VISIBLE
//                    ivDownload.visibility = View.VISIBLE
//                }
//            }
//        }
//    }

}