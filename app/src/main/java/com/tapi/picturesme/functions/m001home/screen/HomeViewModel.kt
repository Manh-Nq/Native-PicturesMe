package com.tapi.picturesme.functions.m001home.screen

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tapi.picturesme.core.database.DownLoadPhoto
import com.tapi.picturesme.core.server.ApiService
import com.tapi.picturesme.functions.m001home.PhotoItemView
import kotlinx.coroutines.launch

open class HomeViewModel : ViewModel() {
    val TAG = HomeViewModel::class.java


    private var _images: MutableLiveData<List<PhotoItemView>> = MutableLiveData()
    val images: LiveData<List<PhotoItemView>>
        get() = _images

    private var currentPage = 1

    private var _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean>
        get() = _loading


    init {

        viewModelScope.launch {
            _loading.value = true
            _images.value = ApiService.retrofitService.getPictures(page = currentPage).map { item ->
                if (DownLoadPhoto().isDownloaded(item)) {
                    PhotoItemView(item, true)

                } else {
                    PhotoItemView(item, false)
                }
            }
            _loading.value = false
        }

    }

    fun getListData(): LiveData<List<PhotoItemView>> {
        return images
    }

    fun getIsloading(): LiveData<Boolean> {
        return loading
    }

    fun loadMore() {
        currentPage++
        viewModelScope.launch {
            _loading.value = true
            val moreList = ApiService.retrofitService.getPictures(page = currentPage).map {
                PhotoItemView(it, false)
            }
            val currList = _images.value?.toMutableList()
            currList?.let {
                currList.addAll(moreList)
                _images.value = currList
            }

            _loading.value = false
        }
    }

}