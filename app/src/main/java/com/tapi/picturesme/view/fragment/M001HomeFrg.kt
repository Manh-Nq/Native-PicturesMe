package com.tapi.picturesme.view.fragment

import android.util.Log
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tapi.picturesme.R
import com.tapi.picturesme.model.PhotoItem
import com.tapi.picturesme.utils.CommonUtils
import com.tapi.picturesme.view.adapter.PhotoAdapter
import com.tapi.picturesme.view.base.BaseFragment
import com.tapi.picturesme.view.event.IPhoto
import com.tapi.picturesme.viewmodel.HomeViewModel
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class M001HomeFrg : BaseFragment() {
    val TAG = M001HomeFrg::class.java.name
    lateinit var rvPhoto: RecyclerView
    lateinit var photoAdapter: PhotoAdapter
    val END_POINT = "https://api.unsplash.com/"
    val CLIENT_ID = "Dq7t7v4s6jR-5hwHV1r9v8wmhlaY-NIi4zlbriJTH44"
    var page = 1
    var per_page = 10
    lateinit var homeViewModel: HomeViewModel

    override fun initViews() {

        homeViewModel = ViewModelProvider(this).get(HomeViewModel::class.java)
        rvPhoto = findViewById(R.id.rv_photo, this)
        rvPhoto.layoutManager = GridLayoutManager(mContext, 2)


        getDataFromSever()
        homeViewModel.listdata.observe(this, Observer { listData ->

            initData(listData)
        })


        rvPhoto.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                val height = rvPhoto.getHeight()

                val diff = height - dy
                if (diff < 1) {
                    Toast.makeText(mContext, "this is scroll", Toast.LENGTH_SHORT).show()
                }
            }
        })
    }

    // TODO: 19-Aug-20
    private fun getDataFromSever() {
        CommonUtils.myCoroutineScope.launch {
            var api: IPhoto = CommonUtils.getRetrofit(END_POINT).create(IPhoto::class.java)
            api.getPhotos(page, per_page, CLIENT_ID)
                .enqueue(object : Callback<List<PhotoItem>> {
                    override fun onResponse(
                        call: Call<List<PhotoItem>>,
                        response: Response<List<PhotoItem>>
                    ) {
                        Log.d(TAG, "onResponse: ${response.body()}")
                        for (item in response.body()!!) {
                            homeViewModel.setDataFromSever(item)
                        }
                    }
                    override fun onFailure(call: Call<List<PhotoItem>>, t: Throwable) {
                        Log.d(TAG, "onFailure: ${t.message}")
                    }

                })
        }

    }


    private fun initData(listData: List<PhotoItem>) {
        photoAdapter = PhotoAdapter(mContext, listData, onClickDownload, onClickItem)
        rvPhoto.adapter = photoAdapter
    }

    override fun getLayoutByID(): Int {
        return R.layout.frg_1
    }

    override fun showPreviousFrg() {
        throw NullPointerException()
    }


    private val onClickDownload: (PhotoItem) -> Unit = {

        Toast.makeText(mContext, it.picture.raw, Toast.LENGTH_SHORT).show()

    }
    private val onClickItem: (PhotoItem) -> Unit = {
        getStorage().photoItem = it
        mCallback.showFragment(M002PicturesFrg().TAG)
    }


}