package com.tapi.picturesme.functions.m001home.screen

import android.app.Activity
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.ConnectivityManager
import android.os.Build
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
import com.tapi.picturesme.R
import com.tapi.picturesme.functions.m001home.PhotoItemView
import com.tapi.picturesme.functions.m001home.adapter.PhotoAdapter
import com.tapi.picturesme.functions.m002gallery.screen.M002GalleryFrg
import com.tapi.picturesme.utils.CommonUtils
import com.tapi.picturesme.view.base.BaseFragment
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch

class M001HomeFrg : BaseFragment(), PhotoAdapter.adapterListener {
    val TAG = M001HomeFrg::class.java.name
    lateinit var rvPhoto: RecyclerView
    lateinit var photoAdapter: PhotoAdapter
    lateinit var homeViewModel: HomeViewModel
    lateinit var btAlbum: FloatingActionButton
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
    val UIThread = MainScope()
    var networkStatus = false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        homeViewModel = ViewModelProvider(this).get(HomeViewModel::class.java)
        photoAdapter = PhotoAdapter(requireContext())
        homeViewModel.getListPhoto().observe(this, Observer {
            photoAdapter.submitList(it)
            photoAdapter.notifyDataSetChanged()
        })

    }


    override fun initViews() {
        rvrt = findViewById(R.id.rt_rv)
        lnNotierr = findViewById(R.id.ln_noti_err)
        tvNotierr = findViewById(R.id.tv_notierr)
        ivPrevious = findViewById(R.id.iv_del, this)
        tbSearch = findViewById(R.id.tb_search)
        ivSfirst = findViewById(R.id.iv_search_first, this)
        tvNoti = findViewById(R.id.tv_notilist)
        edtSearch = findViewById(R.id.edt_search)
        ivRemove = findViewById(R.id.iv_remove, this)
        ivSearch = findViewById(R.id.iv_search, this)
        progressBarLoading = findViewById(R.id.progress_loadmore)
        btAlbum = findViewById(R.id.bt_album, this)

        rvPhoto = findViewById(R.id.rv_photo)
        initData()
        observeViewModel()
        registerReceiverNetwork()
        recycleListener()
        searchPage()

    }

    private fun searchPage() {
        edtSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {

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
        homeViewModel.updateData().observe(this, Observer {
            photoAdapter.submitList(it)
            photoAdapter.notifyDataSetChanged()
        })

        homeViewModel.getIsloading().observe(this, {
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
            homeViewModel.getListPhotoByPage(page = page.toInt()).observe(this, Observer {
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
        Log.d(TAG, "sesmCode: start download ")
        var link = item.photoItem.picture.raw
        if (!CommonUtils.isNetworkConnected(activity as Activity)) {
            showToast("internet error")
            return
        }

        UIThread.launch {
            tvDownload.visibility = View.GONE
            ivCircle.visibility = View.GONE
            progress.visibility = View.VISIBLE
            ivDownload.visibility = View.GONE
        }
        UIThread.launch {
            val resultCode = homeViewModel.downloadPhoto(link)

            Log.d("TAG", "sesm bugger $resultCode")

            if (resultCode == 1) {
               downloadDone(progress, viewBg)
            } else {
                downloadFail(progress, ivCircle, ivDownload, tvDownload)
            }
        }


    }


    private fun downloadDone(progress: ProgressBar, viewBg: View) {
        Log.d(TAG, "sesmCode: Download done")

        showToast("download success")
        progress.visibility = View.GONE
        viewBg.visibility = View.GONE

    }

    private fun downloadFail(
        progress: ProgressBar,
        ivCircle: ImageView,
        ivDownload: ImageView,
        tvDownload: TextView
    ) {
        Log.d(TAG, "sesmCode: Download fail")

        showToast("download fail")
        progress.visibility = View.GONE
        ivCircle.visibility = View.VISIBLE
        ivDownload.visibility = View.VISIBLE
        tvDownload.visibility = View.VISIBLE

    }


    private val networkChangeReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            Log.d(TAG, "onReceive: signIn register")
            try {
                if (isOnline(requireContext())) {
                    networkStatus = true
                    if (homeViewModel.getListPhoto().value!!.size == 0 && networkStatus) {
                        lnNotierr.let { lnNotierr.visibility = View.GONE }
                        homeViewModel.getListPhoto()
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    private fun isOnline(context: Context): Boolean {
        return try {
            val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val netInfo = cm.activeNetworkInfo
            //should check null because in airplane mode it will be null
            netInfo != null && netInfo.isConnected
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    private fun registerReceiverNetwork() {
        Log.d(TAG, "onReceive: register")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            requireActivity().registerReceiver(
                networkChangeReceiver,
                IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION)
            )
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requireActivity().registerReceiver(
                networkChangeReceiver,
                IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION)
            )
        }
    }

    protected fun unRegisterReceiverNetWork() {
        Log.d(TAG, "onReceive: unregister")
        try {
            requireActivity().unregisterReceiver(networkChangeReceiver)
        } catch (e: IllegalArgumentException) {
            e.printStackTrace()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        unRegisterReceiverNetWork()
        UIThread.cancel()
    }
}



