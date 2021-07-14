package com.bbq.webview.viewmodel

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.bbq.base.base.BaseViewModel
import com.bbq.base.utils.getDrawable
import com.bbq.base.view.FabViewModel
import com.bbq.base.view.TitleViewModel
import com.bbq.webview.R
import com.bbq.webview.repo.WebRepo
import com.hjq.toast.ToastUtils

class WebViewModel(application: Application, val repo: WebRepo) : BaseViewModel(application) {
    val mScrollToTop = MutableLiveData(false)
    val mFabVM = FabViewModel(
        onClick = {
            mScrollToTop.value = true
        }
    )

    val mIsFinish = MutableLiveData(false)
    val mTitleVM = TitleViewModel(
        leftAction = {
            mIsFinish.postValue(true)
        },
        title = "",
        rightAction = {

        }
    )

    fun setCollectState(collect: Boolean) {
        mTitleVM.mRightDrawable.set(
            if (collect) R.drawable.collect_red.getDrawable()
            else R.drawable.sc_white_stroke_ico.getDrawable()
        )
    }

    fun setTitle(title: String) {
        mTitleVM.mTitle.set(title)
    }


    suspend fun collect(id: Int?): Boolean {
        return repo.collect(id)
    }

    suspend fun unCollect(id: Int?): Boolean {
        return repo.unCollect(id)
    }
}