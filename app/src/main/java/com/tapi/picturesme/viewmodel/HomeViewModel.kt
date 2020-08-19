package com.tapi.picturesme.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.tapi.picturesme.model.PhotoItem

open class HomeViewModel : ViewModel {
    open val TAG = HomeViewModel::class.java


    open var listdata: MutableLiveData<List<PhotoItem>> = MutableLiveData()
    open var listtmp: MutableList<PhotoItem> = ArrayList()


    constructor() {
        listdata.value = listOf()
        listtmp = ArrayList()
    }


    fun setDataFromSever(photoItem: PhotoItem) {
        listtmp.add(photoItem)
        listdata.value = listtmp
    }


}