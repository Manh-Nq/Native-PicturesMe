package com.tapi.picturesme.functions.home.screen

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tapi.picturesme.functions.home.PhotoItemView
import com.tapi.picturesme.utils.getRetrofit
import kotlinx.coroutines.launch

open class HomeViewModel : ViewModel() {
    val TAG = HomeViewModel::class.java


    private var _images: MutableLiveData<List<PhotoItemView>> = MutableLiveData()
    val images: LiveData<List<PhotoItemView>>
        get() = _images

    private lateinit var photo: PhotoItemView

    private var currPage = 1

    private var _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean>
        get() = _loading

    init {
        viewModelScope.launch {
            _loading.value = true
            _images.value = getRetrofit.retrofitService.getPictures(page = currPage).map {
                PhotoItemView(it, false)
            }
            _loading.value = false
        }
    }

    fun loadMore() {
        currPage++
        viewModelScope.launch {
            _loading.value = true
            val moreList = getRetrofit.retrofitService.getPictures(page = currPage).map {
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