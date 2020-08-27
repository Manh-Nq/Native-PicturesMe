package com.tapi.picturesme.functions.m001home.screen

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.*
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
import kotlinx.coroutines.CoroutineExceptionHandler
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
    lateinit var ivSfirst: ImageView
    lateinit var ivPrevious: ImageView
    lateinit var tbSearch: TableRow
    lateinit var lnNotierr: LinearLayout
    lateinit var tvNotierr: TextView
    lateinit var rvrt: RelativeLayout




    override fun initViews() {
        rvrt = findViewById(R.id.rt_rv, this)
        lnNotierr = findViewById(R.id.ln_noti_err, this)
        tvNotierr = findViewById(R.id.tv_notierr, this)
        ivPrevious = findViewById(R.id.iv_del, this)
        tbSearch = findViewById(R.id.tb_search, this)
        ivSfirst = findViewById(R.id.iv_search_first, this)
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
        Log.d(TAG, "observeViewModel: check valid")

        if (!CommonUtils.isNetworkConnected(activity as Activity)) {
            tvNoti.visibility = View.VISIBLE

            showToast("internet error!!!")
            return
        }

        homeViewModel.getListPhoto()?.observe(this, Observer {
            photoAdapter.submitList(it)
        })

        homeViewModel.getIsloading().observe(this, Observer {
            progressBarLoading.visibility = if (it == 1) View.VISIBLE else View.GONE
            Log.d(TAG, "observeViewModel: $it")
            when (it) {
                2 -> showNotiErr("401")
                3 -> showNotiErr("404")
                4 -> showNotiErr("500")
                5 -> showNotiErr("504")
                6 -> showNotiErr("error")
                7 -> showNotiErr("Disconnect")
            }
        })


    }


    private fun showNotiErr(err: String) {
        lnNotierr.visibility = View.VISIBLE
        tvNotierr.text = err
        ivSfirst.visibility = View.VISIBLE
        tbSearch.visibility = View.GONE
        rvrt.visibility = View.GONE
        showToast("sever error!!!")
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

                    if (homeViewModel.loading.value == 0) {

                        if ((visibleItemCount + passVisibleItem) > total) {
                            if (!CommonUtils.isNetworkConnected(activity as Activity)) {
                                showToast("internet error")
                                return
                            }
                            Log.d(TAG, "onScrolled: API load more")

                            var i = homeViewModel.loadMore()

                            edtSearch.setText(i.toString())
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
            R.id.iv_search_first -> showTableSearch()
            R.id.iv_del-> hideTableSearch()
            R.id.iv_search -> searchPhotobyPage(textOf(edtSearch))
        }
    }

    private fun hideTableSearch() {
        ivSfirst.visibility= View.VISIBLE
        tbSearch.visibility = View.GONE
    }

    private fun showTableSearch() {
        ivSfirst.visibility= View.GONE
        tbSearch.visibility = View.VISIBLE
    }

    private fun searchPhotobyPage(page: String) {
        if (checkValid(page)) {

            homeViewModel.getListPhotoByPage(page = page.toInt())?.observe(this, Observer {

                if (homeViewModel.getIsloading().value != 1 && homeViewModel.getIsloading().value != 0) {
                    lnNotierr.visibility = View.VISIBLE
                    rvrt.visibility = View.GONE
                    tbSearch.visibility = View.GONE
                    Log.d(TAG, "searchPhotobyPag11e: ${homeViewModel.getIsloading().value}")
                    showToast("sever error!!!")
                }

                if (it == null || it.size < 0) {
                    Log.d(TAG, "searchPhotobyPage: ${it.size}")
                    tvNoti.visibility = View.VISIBLE
                    showToast("internet error!!!")

                }
                photoAdapter.submitList(it)
                photoAdapter.notifyDataSetChanged()
            })
        }

    }

    private fun checkValid(page: String): Boolean {
        if (page.isEmpty()) {
            edtSearch.setError("need to enter information")
            edtSearch.requestFocus()
            return false
        }
        if (page.length>=10) {
            Log.d(TAG, "checkValid: $page")
            edtSearch.setError("number format error")
            edtSearch.requestFocus()
            return false
        }
        if (!CommonUtils.isNetworkConnected(activity as Activity)) {
            showToast("internet err")
            return false
        }
        return true
    }


    private fun toMyAlbum() {

        mCallback.showFragment(M002GalleryFrg().TAG, false)
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
        val handler = CoroutineExceptionHandler { _, exception ->
            Log.d("TAG", "CoroutineExceptionHandler got $exception")
            showToast("download fail")
            progress.visibility = View.GONE
            ivCircle.visibility = View.VISIBLE
            ivDownload.visibility = View.VISIBLE
            tvDownload.visibility = View.VISIBLE
        }

        var link = item.photoItem.picture.raw

        if (!CommonUtils.isNetworkConnected(activity as Activity)) {
            showToast("internet error")
            return
        }
        tvDownload.visibility = View.GONE
        ivCircle.visibility = View.GONE
        progress.visibility = View.VISIBLE
        ivDownload.visibility = View.GONE

        CommonUtils.myCoroutineScope.launch(handler) {
            withContext(Dispatchers.Default) {

                    var newUrl = link + "&w=" + 300 + "&dpi=" + 1
                    Log.d(TAG, "URLcustom: $newUrl")
                Log.d(TAG, "URLOffical: $link ")
                /**dowmload photo from sever */

                response = ApiService.retrofitService.getPhotoFromSever(newUrl)
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


    }


}