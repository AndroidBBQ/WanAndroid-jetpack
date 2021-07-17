package com.bbq.base.utils

import androidx.lifecycle.MutableLiveData

object LiveDataBus {

    private val mMapAll = HashMap<String, Any>()
    fun <T> with(key: String): MutableLiveData<T> {
        if (!mMapAll.containsKey(key)) {
            mMapAll[key] = MutableLiveData<T>()
        }
        return (mMapAll[key] as MutableLiveData<T>)
    }

}