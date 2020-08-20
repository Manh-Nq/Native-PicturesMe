package com.tapi.picturesme.view.event

import com.tapi.picturesme.functions.home.PhotoItemView

interface OnPhotoCallBack {
    fun clickItemDownLoad();
     fun ShowToast(item: PhotoItemView)
}