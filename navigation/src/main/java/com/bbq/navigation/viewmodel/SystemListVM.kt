package com.bbq.navigation.viewmodel

import android.app.Application
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.bbq.base.base.BaseViewModel
import com.bbq.base.rository.StateLiveData
import com.bbq.base.view.TitleViewModel
import com.bbq.navigation.bean.ArticleBean
import com.bbq.navigation.repo.NavRepo
import com.bbq.net.model.BasePagingResult
import kotlinx.coroutines.launch

class SystemListVM(application: Application, val repo: NavRepo) : BaseViewModel(application) {
    val mIsFinish = MutableLiveData(false)
    val mTitleVM = TitleViewModel(
        leftAction = {
            mIsFinish.postValue(true)
        },
        title = "",
        rightAction = {

        }
    )

    fun setTitle(title: String) {
        mTitleVM.mTitle.set(title)
    }

    val mArticleList = StateLiveData<BasePagingResult<List<ArticleBean>>>()

    fun articleList(position: Int, cid: Int) {
        viewModelScope.launch {
            repo.getArticleList(position, cid, mArticleList)
        }
    }


    suspend fun collect(id: Int?): Boolean {
        return repo.collect(id)
    }

    suspend fun unCollect(id: Int?): Boolean {
        return repo.unCollect(id)
    }
}