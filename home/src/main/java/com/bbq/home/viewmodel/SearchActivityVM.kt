package com.bbq.home.viewmodel

import android.app.Application
import androidx.databinding.ObservableField
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.bbq.base.base.BaseViewModel
import com.bbq.base.utils.SpUtils
import com.bbq.home.bean.ArticleBean
import com.bbq.home.bean.HotKeyBean
import com.bbq.home.repo.HomeRepo
import com.bbq.net.model.ResultState
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SearchActivityVM(application: Application, val repo: HomeRepo) : BaseViewModel(application) {

    val mRecyclerVisible = ObservableField(false)

    val mHistoryList = MutableLiveData<MutableList<String>>()
    fun getHistory() {
        val history = SpUtils.getString("search_history")
        if (history == null) {
            mHistoryList.postValue(mutableListOf())
        } else {
            val hisList = Gson().fromJson<MutableList<String>>(
                history,
                object : TypeToken<MutableList<String>>() {}.type
            )
            mHistoryList.postValue(hisList)
        }
    }

    val mArticleList = MutableLiveData<MutableList<ArticleBean>>()
    val mMaxPage = MutableLiveData(0)
    val mLoadError = MutableLiveData(false)

    fun searchArticles(page: Int, key: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val articleResult = repo.searchArticles(page, key)
            if (articleResult is ResultState.Success) {
                mLoadError.postValue(false)
                if (page == 0) {
                    if (articleResult.data?.datas.isNullOrEmpty()) {
                        //首页没有数据
                        mArticleList.postValue(mutableListOf())
                    } else {
                        mArticleList.postValue(articleResult.data?.datas!!)
                    }
                } else {
                    mArticleList.value?.addAll(articleResult.data!!.datas)
                    mArticleList.postValue(mArticleList.value)
                }
                //判断page总数
                mMaxPage.postValue(articleResult.data?.pageCount)
            } else if (articleResult is ResultState.Error) {
                mLoadError.postValue(true)
            }

        }
    }


    val mHotKeyList = MutableLiveData<MutableList<HotKeyBean>>()
    fun getHotKeys() {
        val hotKeys = SpUtils.getString("hot_keys")
        if (hotKeys.isNullOrEmpty()) {
            viewModelScope.launch(Dispatchers.IO) {
                val hotResult = repo.getHotKey()
                if (hotResult is ResultState.Success) {
                    mHotKeyList.postValue(hotResult.data!!)
                    SpUtils.put("hot_keys", Gson().toJson(hotResult.data))
                } else if (hotResult is ResultState.Error) {
                    mHotKeyList.postValue(mutableListOf())
                }
            }
        } else {
            val keyList = Gson().fromJson<MutableList<HotKeyBean>>(
                hotKeys,
                object : TypeToken<MutableList<HotKeyBean>>() {}.type
            )
            mHotKeyList.postValue(keyList)
        }

    }


    suspend fun collect(id: Int?): Boolean {
        return repo.collect(id)
    }

    suspend fun unCollect(id: Int?): Boolean {
        return repo.unCollect(id)
    }

}