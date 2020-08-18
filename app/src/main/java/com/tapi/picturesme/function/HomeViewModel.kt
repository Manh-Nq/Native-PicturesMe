package com.tapi.picturesme.function

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

open class HomeViewModel : ViewModel {
    open val TAG = HomeViewModel::class.java


    open var mScore: MutableLiveData<Int> = MutableLiveData()

    constructor() {
        mScore.value = 0
    }


    fun setScore(i: Int) {
        mScore.value = mScore.value!! + i
    }

}