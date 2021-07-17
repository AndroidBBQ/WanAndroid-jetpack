package com.bbq.navigation.viewmodel

import android.app.Application
import androidx.lifecycle.viewModelScope
import com.bbq.base.base.BaseViewModel
import com.bbq.base.rository.StateLiveData
import com.bbq.navigation.bean.ArticleBean
import com.bbq.navigation.bean.NavTabBean
import com.bbq.navigation.bean.PublicBean
import com.bbq.navigation.bean.TreeBean
import com.bbq.navigation.repo.NavRepo
import com.bbq.net.model.BasePagingResult
import kotlinx.coroutines.launch

class NavTabVM(application: Application, val navRepo: NavRepo) : BaseViewModel(application) {

    val mLeftList = StateLiveData<List<NavTabBean>>()

    fun getNavList() {
        viewModelScope.launch {
            navRepo.getNavList(mLeftList)
        }
    }

    val mTreeList = StateLiveData<List<TreeBean>>()
    fun getTreeList() {
        viewModelScope.launch {
            navRepo.getTreeList(mTreeList)
        }
    }


    val mWeChatList = StateLiveData<List<PublicBean>>()
    fun getWeChatList() {
        viewModelScope.launch {
            navRepo.weChatList(mWeChatList)
        }
    }

    val mWeChatDetailList = StateLiveData<BasePagingResult<List<ArticleBean>>>()
    fun getWeChatDetailList(id: Int, page: Int) {
        viewModelScope.launch {
            navRepo.weChatListDetail(id, page, mWeChatDetailList)
        }
    }

    val mProjectList = StateLiveData<BasePagingResult<List<ArticleBean>>>()
    fun getProjectList(page: Int) {
        viewModelScope.launch {
            navRepo.projectList(page, mProjectList)
        }
    }

    val mProjectLeft = StateLiveData<List<PublicBean>>()
    fun getProjectLeft() {
        viewModelScope.launch {
            navRepo.projectLeftList(mProjectLeft)
        }
    }

    val mProjectDetailList = StateLiveData<BasePagingResult<List<ArticleBean>>>()
    fun getProjectDetail(page: Int, id: Int) {
        viewModelScope.launch {
            navRepo.projectDetailList(id, page, mProjectDetailList)
        }
    }

    suspend fun collect(id: Int?): Boolean {
        return navRepo.collect(id)
    }

    suspend fun unCollect(id: Int?): Boolean {
        return navRepo.unCollect(id)
    }
}