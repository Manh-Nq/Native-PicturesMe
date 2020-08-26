package com.tapi.picturesme.functions.m003detail.screen

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tapi.picturesme.App
import com.tapi.picturesme.core.database.DownLoadPhoto
import com.tapi.picturesme.core.database.entity.PhotoEntity
import kotlinx.coroutines.launch

class DetailViewmodel : ViewModel() {

    var _data = PhotoEntity()
    var data: MutableLiveData<PhotoEntity> = MutableLiveData()

    init {
        _data = App.storageCommon.photoItem
        data.postValue(_data)
    }

    fun getImage(): MutableLiveData<PhotoEntity> {
        return data
    }

    fun deteleImage() = viewModelScope.launch {
        App.photoDatabase.photoDAO.deletePhoto(_data)
        DownLoadPhoto().deleteFile(_data.path)


        Log.d("TAG", "deteleImage: ${_data.path}  + ${DownLoadPhoto().deleteFile(_data.path)}")

    }
}