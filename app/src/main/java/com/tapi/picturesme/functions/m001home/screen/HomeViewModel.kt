package com.tapi.picturesme.functions.m001home.screen

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tapi.picturesme.core.database.DownLoadPhoto
import com.tapi.picturesme.core.server.ApiService
import com.tapi.picturesme.functions.m001home.PhotoItemView
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.launch

open class HomeViewModel : ViewModel() {
    val TAG = HomeViewModel::class.java

    private var _images: MutableLiveData<List<PhotoItemView>> = MutableLiveData()
    val images: LiveData<List<PhotoItemView>>
        get() = _images

    private var currentPage = 1

    private var _loading = MutableLiveData<Int>()
    val loading: LiveData<Int>
        get() = _loading


    fun getListPhoto(): LiveData<List<PhotoItemView>>? {
        val handler = CoroutineExceptionHandler { _, exception ->
            _loading.value = 7
        }

        Log.d("TAG", "loadList: API listPhoto calling....... ")
        var job = viewModelScope.launch(handler) {
            Log.d("TAG", "getListPhoto: start call API")

            var response = ApiService.retrofitService.getPictures(page = currentPage)

            checkValide(response.code())

            _loading.value = 1
            response.body()?.let {
                Log.d("TAG", "comfirmPhoto: ${it.size}")

                _images.postValue(it.map {

                    if (DownLoadPhoto().isDownloaded(it)) {
                        PhotoItemView(it, true)

                    } else {
                        PhotoItemView(it, false)
                    }
                })
            }
            _loading.value = 0
        }

        return images
    }

    private fun checkValide(code: Int) {
        if (code == 200) {
            _loading.value = 1
            return
        }
        if (code == 401) {
            _loading.value = 2
            return
        }
        if (code == 404) {
            _loading.value = 3
            return
        }
        if (code == 500) {
            _loading.value = 4
            return
        }
        if (code == 504) {
            _loading.value = 5
            return
        }
        if (code != 200) {
            _loading.value = 6
            return
        }

    }

    fun getListPhotoByPage(page: Int): LiveData<List<PhotoItemView>>? {
        val handler = CoroutineExceptionHandler { _, exception ->
            _loading.value = 7
        }
        Log.d("TAG", "loadMore : API getListPhotoByPage calling....... ")

        viewModelScope.launch(handler) {

            var response = ApiService.retrofitService.getPictures(page = page)

            _loading.value = 1
            checkValide(response.code())
            _images.postValue(response.body()?.map {

                Log.d("TAG", "comfirmPhoto: $it")
                if (DownLoadPhoto().isDownloaded(it)) {
                    PhotoItemView(it, true)

                } else {
                    PhotoItemView(it, false)
                }
            })
            _loading.value = 0
            }

        return images
    }


    fun getIsloading(): LiveData<Int> {
        return loading
    }

    fun loadMore(): Int {
        currentPage++
        val handler = CoroutineExceptionHandler { _, exception ->
            _loading.value = 7
            Log.d("TAG", "CoroutineExceptionHandler got $exception")
        }
        viewModelScope.launch(handler) {

            callGetPhotoToLoadMore(currentPage)
        }

        return currentPage
    }

    suspend fun callGetPhotoToLoadMore(currentPage: Int): LiveData<List<PhotoItemView>>? {

        Log.d("TAG", "loadMore: API loadMore calling....... ")
        _loading.value = 1
        val moreList = ApiService.retrofitService.getPictures(page = currentPage)
            .body()?.map {
                if (DownLoadPhoto().isDownloaded(it)) {
                    PhotoItemView(it, true)
                } else {
                    PhotoItemView(it, false)
                }
            }
        val response = ApiService.retrofitService.getPictures(page = currentPage)
        checkValide(response.code())
        val currList = _images.value?.toMutableList()
        currList?.let {
            if (moreList != null) {
                currList.addAll(moreList)
            }
            _images.value = currList
        }

        _loading.value = 0
        return _images


    }

}




