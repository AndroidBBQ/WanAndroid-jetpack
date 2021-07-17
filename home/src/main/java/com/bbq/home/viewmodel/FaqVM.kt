package com.bbq.home.viewmodel

import android.app.Application
import androidx.databinding.ObservableField
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.bbq.base.R
import com.bbq.base.base.BaseViewModel
import com.bbq.base.rository.StateLiveData
import com.bbq.base.utils.getDrawable
import com.bbq.base.view.TitleViewModel
import com.bbq.home.bean.ArticleBean
import com.bbq.home.repo.HomeRepo
import com.bbq.net.model.BasePagingResult
import kotlinx.coroutines.launch

class FaqVM(application: Application, val homeRepo: HomeRepo) : BaseViewModel(application) {

    val mTitleVM = TitleViewModel(
        leftDrawable = null
    )

    fun setTitle(title: String) {
        mTitleVM.mTitle.set(title)
    }

    val mFinish = MutableLiveData(false)
    fun setLeftIcon() {
        mTitleVM.leftDrawable = R.drawable.abc_vector_test.getDrawable()
        mTitleVM.leftAction = {
            mFinish.postValue(true)
        }
    }

    fun setLeftIconNull() {
        mTitleVM.leftDrawable = null
    }

    val mFaqList = StateLiveData<BasePagingResult<List<ArticleBean>>>()
    fun getFaqList(position: Int) {
        viewModelScope.launch {
            homeRepo.getFaqList(position, mFaqList)
        }
    }


    val mCollectionList = StateLiveData<BasePagingResult<List<ArticleBean>>>()
    fun getCollectList(position: Int) {
        viewModelScope.launch {
            homeRepo.getCollectionList(position, mCollectionList)
        }
    }

    suspend fun collect(id: Int?): Boolean {
        return homeRepo.collect(id)
    }

    suspend fun unCollect(id: Int?): Boolean {
        return homeRepo.unCollect(id)
    }


}