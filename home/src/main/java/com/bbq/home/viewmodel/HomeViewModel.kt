package com.bbq.home.viewmodel

import android.app.Application
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.bbq.base.base.BaseViewModel
import com.bbq.home.bean.BannerBean
import com.bbq.home.bean.HotKeyBean
import com.bbq.home.repo.HomeRepo
import com.bbq.net.model.ResultState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class HomeViewModel(app: Application, val repo: HomeRepo) : BaseViewModel(app) {

    val mHotKeyList = MutableLiveData<MutableList<HotKeyBean>>()
    fun getHotKeys() {
        viewModelScope.launch(Dispatchers.IO) {
            val hotResult = repo.getHotKey()
            if (hotResult is ResultState.Success) {
                mHotKeyList.postValue(hotResult.data)
            } else if (hotResult is ResultState.Error) {
                toast(hotResult.exception.msg)
            }
        }
    }

    val mBannerList = MutableLiveData<MutableList<BannerBean>>()
    fun getBannerList() {
        viewModelScope.launch(Dispatchers.IO) {
            repo.getBanners()
        }
    }
}