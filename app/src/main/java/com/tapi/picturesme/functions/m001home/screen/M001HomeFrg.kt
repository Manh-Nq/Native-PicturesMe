package com.tapi.picturesme.functions.m001home.screen

import android.content.Context
import android.content.ContextWrapper
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.tapi.picturesme.App
import com.tapi.picturesme.R
import com.tapi.picturesme.core.database.DownLoadPhoto
import com.tapi.picturesme.core.database.entity.PhotoEntity
import com.tapi.picturesme.core.server.ApiService
import com.tapi.picturesme.functions.m001home.PhotoItemView
import com.tapi.picturesme.functions.m001home.adapter.PhotoAdapter
import com.tapi.picturesme.functions.m002gallery.screen.M002GalleryFrg
import com.tapi.picturesme.utils.CommonUtils
import com.tapi.picturesme.view.base.BaseFragment
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.ResponseBody
import java.io.File

class M001HomeFrg : BaseFragment(), PhotoAdapter.adapterListener {
    val TAG = M001HomeFrg::class.java.name
    lateinit var rvPhoto: RecyclerView
    lateinit var photoAdapter: PhotoAdapter
    lateinit var homeViewModel: HomeViewModel
    lateinit var btAlbum: FloatingActionButton
    lateinit var response: ResponseBody
    lateinit var bitMap: Bitmap
    lateinit var progressBarLoading: ProgressBar

    lateinit var edtSearch: EditText
    lateinit var ivRemove: ImageView
    lateinit var ivSearch: ImageView
    lateinit var tvNoti: TextView

    override fun initViews() {

        tvNoti = findViewById(R.id.tv_notilist, this)
        edtSearch = findViewById(R.id.edt_search, this)
        ivRemove = findViewById(R.id.iv_remove, this)
        ivSearch = findViewById(R.id.iv_search, this)
        progressBarLoading = findViewById(R.id.progress_loadmore, this)
        btAlbum = findViewById(R.id.bt_album, this)
        homeViewModel = ViewModelProvider(this).get(HomeViewModel().TAG)
        rvPhoto = findViewById(R.id.rv_photo, this)


        initData()
        observeViewModel()

        recycleListener()
        searchPage()

    }

    private fun searchPage() {
        edtSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {

                //
                if (textOf(edtSearch).length > 0) {
//                    ivSearch.visibility = View.VISIBLE
                    ivRemove.visibility = View.VISIBLE
                } else {
                    ivRemove.visibility = View.GONE
//                    ivSearch.visibility = View.GONE
                }
            }

            override fun afterTextChanged(s: Editable) {}
        })
    }

    private fun initData() {
        rvPhoto.layoutManager = GridLayoutManager(mContext, 2)

        photoAdapter = PhotoAdapter(mContext)
        photoAdapter.setCallBackAdapterHome(this)
        rvPhoto.adapter = photoAdapter
    }

    private fun observeViewModel() {
        homeViewModel.getListPhoto().observe(this, Observer {
            if (it.isEmpty()) {
                tvNoti.visibility = View.VISIBLE
            }
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
                            var i = homeViewModel.loadMore()
                            edtSearch.setText(i.toString())
                            photoAdapter.notifyDataSetChanged()

                        }

                    }

                }

            }

        })

    }


    override fun onClick(p0: View) {
        when (p0.id) {
            R.id.bt_album -> toMyAlbum()
            R.id.iv_remove -> edtSearch.setText("")
//            R.id.iv_search -> searchPhotobyPage(textOf(edtSearch))
        }
    }

//    private fun searchPhotobyPage(page: String) {
//        if (checkValid(page)) {
//            var pageNew = page.toInt()
//            homeViewModel.getListPhotoByPage(page = pageNew).observe(this, Observer {
//                if (it.isEmpty()) {
//                    tvNoti.visibility = View.VISIBLE
//                }
//                photoAdapter.submitList(it)
//                photoAdapter.notifyDataSetChanged()
//            })
//        }
//
//    }

    private fun checkValid(page: String): Boolean {
        if (page.isEmpty()) {
            edtSearch.setError("need to enter information")
            edtSearch.requestFocus()
            return false
        }

        return true

    }


    private fun toMyAlbum() {
        getStorage().page = textOf(edtSearch).toInt()
        mCallback.showFragment(M002GalleryFrg().TAG)
    }

    // TODO: 19-Aug-20


    override fun getLayoutByID(): Int {
        return R.layout.m001_home_frg
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
            var link = item.photoItem.picture.raw

            CommonUtils.myCoroutineScope.launch {
                withContext(Dispatchers.Default) {

//                    var newUrl = link + "&w=" + 300 + "&dpi=" + 1
//                    Log.d(TAG, "URLcustom: $newUrl")
                    Log.d(TAG, "URLOffical: $link ")
                    response = ApiService.retrofitService.getPhotoFromSever(link)
                    bitMap = BitmapFactory.decodeStream(response.byteStream())


                    /** save image to internal */
                    val path = link.substring(link.indexOf('-') + 1, link.indexOf('?')) + ".png"
                    try {
                        DownLoadPhoto().saveToInternalStorage(bitMap, path)
                    } catch (e: Exception) {
                        showToast("Not find File path !!!")
                    }

                    val cw = ContextWrapper(App.instance.getApplicationContext())
                    val directory: File = cw.getDir("imageDir", Context.MODE_PRIVATE)

                    var photo = PhotoEntity()
                    photo.path = "$directory/$path"
                    photo.isDownload = true
                    App.photoDatabase.photoDAO.savePhoto(photo)
                    item.isDownloaded = true

                }
                showToast("download success")
                progress.visibility = View.GONE
                viewBg.visibility = View.GONE

            }
        } catch (e: Exception) {
            e.printStackTrace()
            showToast("download fail")
            progress.visibility = View.GONE
            ivCircle.visibility = View.VISIBLE
            ivDownload.visibility = View.VISIBLE
            tvDownload.visibility = View.VISIBLE
        }

    }


}