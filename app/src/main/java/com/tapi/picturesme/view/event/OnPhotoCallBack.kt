package com.tapi.picturesme.view.event

import com.tapi.picturesme.functions.m001home.PhotoItemView

interface OnPhotoCallBack {
    fun clickItemDownLoad();
     fun ShowToast(item: PhotoItemView)
}