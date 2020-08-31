package com.tapi.picturesme.functions.m002gallery.screen

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tapi.picturesme.App
import com.tapi.picturesme.core.database.entity.PhotoEntity
import com.tapi.picturesme.functions.m001home.PhotoItemView
import kotlinx.coroutines.launch

class GalleryViewModel : ViewModel() {

    private var _listPhoto :MutableLiveData<List<PhotoEntity>> = MutableLiveData()
    val listPhoto:MutableLiveData<List<PhotoEntity>>
    get()= _listPhoto

    init {
       viewModelScope.launch {
           _listPhoto.value = App.photoDatabase.photoDAO.getListPhoto()
       }
    }

    fun getListPhoto():LiveData<List<PhotoEntity>>{
        return listPhoto
    }

}