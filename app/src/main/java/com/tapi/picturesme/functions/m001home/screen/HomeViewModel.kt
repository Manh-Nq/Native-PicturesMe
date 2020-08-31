package com.tapi.picturesme.functions.m001home.screen

import android.content.Context
import android.content.ContextWrapper
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tapi.picturesme.App
import com.tapi.picturesme.core.database.entity.PhotoEntity
import com.tapi.picturesme.core.database.isDownloaded
import com.tapi.picturesme.core.database.saveToInternalStorage
import com.tapi.picturesme.core.server.ApiService
import com.tapi.picturesme.functions.m001home.PhotoItemView
import com.tapi.picturesme.utils.CommonUtils
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.ResponseBody
import java.io.File

open class HomeViewModel : ViewModel() {
    val LOAD_DONE = 0
    val LOADING = 1
    val NON_ACCESS = 2
    val NON_HTTP = 3
    val INTERNAL_SERVER_ERROR = 4
    val TOO_TIME_OUT = 5
    val SERVER_ERR = 6
    val COROUTINE_ERR = 7
    lateinit var response: ResponseBody
    lateinit var bitMap: Bitmap

    private var listData: ArrayList<PhotoItemView> = ArrayList()
    private var _images: MutableLiveData<List<PhotoItemView>> = MutableLiveData()

    val images: LiveData<List<PhotoItemView>>
        get() = _images

    private var currentPage = 1

    private var _loading = MutableLiveData<Int>()
    val loading: LiveData<Int>
        get() = _loading

    private var _downloadState = MutableLiveData<Int>()
    val downloadState: LiveData<Int>
        get() = _downloadState


    fun getListPhoto(): LiveData<List<PhotoItemView>> {
        val handler = CoroutineExceptionHandler { _, exception ->
            _loading.value = COROUTINE_ERR
        }

        Log.d("TAG", "loadList: API listPhoto calling....... ")
        var job = viewModelScope.launch(handler) {
            Log.d("TAG", "getListPhoto: start call API")

            var response = ApiService.retrofitService.getPictures(page = currentPage)

            checkValide(response.code())

            _loading.value = LOADING
            response.body()?.let {
                Log.d("TAG", "comfirmPhoto: ${it.size}")


                _images.postValue(it.map {

                    if (isDownloaded(it)) {
                        PhotoItemView(it, true)

                    } else {
                        PhotoItemView(it, false)
                    }
                })
            }
            _loading.value = LOAD_DONE
        }

        return images
    }


    fun updateData(): LiveData<List<PhotoItemView>> {

        viewModelScope.launch {
            var index = 0
            if (_images.value != null) {
                for (item in _images.value!!) {
                    if (isDownloaded(item.photoItem)) {
                        var newPhotoItemView = item.copy()
                        newPhotoItemView.isDownloaded = false
                        var photoItemNew = newPhotoItemView.photoItem.copy()
                        photoItemNew.isDownloaded = false
                        newPhotoItemView.photoItem = photoItemNew
                        listData = (_images.value as ArrayList<PhotoItemView>?)!!

                        if (index < listData.size) return@launch
                        listData.set(index, newPhotoItemView)

                        _images.postValue(listData)
                    }
                    index++
                }
            }
        }
        return images
    }

    private fun checkValide(code: Int) {
        if (code == 200) {
            _loading.value = LOADING
            return
        }
        if (code == 401) {
            _loading.value = NON_ACCESS
            return
        }
        if (code == 404) {
            _loading.value = NON_HTTP
            return
        }
        if (code == 500) {
            _loading.value = INTERNAL_SERVER_ERROR
            return
        }
        if (code == 504) {
            _loading.value = TOO_TIME_OUT
            return
        }
        if (code != 200) {
            _loading.value = SERVER_ERR
            return
        }

    }

    fun getListPhotoByPage(page: Int): LiveData<List<PhotoItemView>> {
        val handler = CoroutineExceptionHandler { _, exception ->
            _loading.value = COROUTINE_ERR
        }
        Log.d("TAG", "loadMore : API getListPhotoByPage calling....... ")

        viewModelScope.launch(handler) {

            var response = ApiService.retrofitService.getPictures(page = page)

            _loading.value = LOADING
            checkValide(response.code())
            _images.postValue(response.body()?.map {

                Log.d("TAG", "comfirmPhoto: $it")
                if (isDownloaded(it)) {
                    PhotoItemView(it, true)

                } else {
                    PhotoItemView(it, false)
                }
            })
            _loading.value = LOAD_DONE
            }

        return images
    }


    fun getIsloading(): LiveData<Int> {
        return loading
    }

    fun loadMore(): Int {
        currentPage++

        val handler = CoroutineExceptionHandler { _, exception ->
            _loading.value = COROUTINE_ERR
            Log.d("TAG", "CoroutineExceptionHandler got $exception")
        }
        viewModelScope.launch(handler) {

            callGetPhotoToLoadMore(currentPage)
        }

        return currentPage
    }

    suspend fun callGetPhotoToLoadMore(currentPage: Int): LiveData<List<PhotoItemView>>? {

        Log.d("TAG", "loadMore: API loadMore calling....... ")
        _loading.value = LOADING
        val moreList = ApiService.retrofitService.getPictures(page = currentPage)
            .body()?.map {
                if (isDownloaded(it)) {
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

        _loading.value = LOAD_DONE
        return _images


    }

    fun downloadPhoto(link: String): LiveData<Int> {
        var handler = CoroutineExceptionHandler { _, exception ->
            _downloadState.postValue(1)
        }
        CommonUtils.myCoroutineScope.launch(handler) {
            withContext(Dispatchers.Default) {

                var newUrl = "$link&w=1080&dpi=1"

                /**dowmload photo from sever */

                response = ApiService.retrofitService.getPhotoFromSever(newUrl)
                bitMap = BitmapFactory.decodeStream(response.byteStream())


                /** save image to internal */

                val path = link.substring(link.indexOf('-') + 1, link.indexOf('?')) + ".png"
                try {
                    saveToInternalStorage(bitMap, path)
                } catch (e: Exception) {
                    _downloadState.postValue(2)
                }

                val cw = ContextWrapper(App.instance.getApplicationContext())
                val directory: File = cw.getDir("imageDir", Context.MODE_PRIVATE)

                var photo = PhotoEntity()
                photo.path = "$directory/$path"
                photo.isDownload = true
                App.photoDatabase.photoDAO.savePhoto(photo)


            }

            _downloadState.postValue(0)


        }
        return downloadState
    }
}




