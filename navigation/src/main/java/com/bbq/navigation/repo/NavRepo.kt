package com.bbq.navigation.repo

import com.bbq.base.rository.BaseRepository
import com.bbq.base.rository.StateLiveData
import com.bbq.navigation.api.NavApi
import com.bbq.navigation.bean.ArticleBean
import com.bbq.navigation.bean.NavTabBean
import com.bbq.navigation.bean.PublicBean
import com.bbq.navigation.bean.TreeBean
import com.bbq.net.model.BasePagingResult
import com.bbq.net.model.ResultState

class NavRepo(val navApi: NavApi) : BaseRepository() {
    suspend fun getNavList(leftList: StateLiveData<List<NavTabBean>>) {
        executeRequest({ navApi.naviList() }, leftList)
    }

    suspend fun getTreeList(treeList: StateLiveData<List<TreeBean>>) {
        executeRequest({ navApi.treeList() }, treeList)
    }

    suspend fun getArticleList(
        page: Int,
        cid: Int,
        articleList: StateLiveData<BasePagingResult<List<ArticleBean>>>
    ) {
        executeRequest({ navApi.articleList(page, cid) }, articleList)
    }

    suspend fun collect(id: Int?): Boolean {
        val result = callRequest { handleResponse(navApi.collect(id)) }
        return result is ResultState.Success
    }

    suspend fun unCollect(id: Int?): Boolean {
        val result = callRequest { handleResponse(navApi.unCollect(id)) }
        return result is ResultState.Success
    }


    suspend fun weChatList(list: StateLiveData<List<PublicBean>>) {
        executeRequest({ navApi.weChatList() }, list)
    }

    suspend fun weChatListDetail(
        id: Int,
        page: Int,
        list: StateLiveData<BasePagingResult<List<ArticleBean>>>
    ) {
        executeRequest({ navApi.weChatListDetail(id, page) }, list)
    }

    suspend fun projectList(page: Int, list: StateLiveData<BasePagingResult<List<ArticleBean>>>) {
        executeRequest({ navApi.projectList(page) }, list)
    }


    suspend fun projectLeftList(list: StateLiveData<List<PublicBean>>) {
        executeRequest({ navApi.projectTreeList() }, list)
    }


    suspend fun projectDetailList(
        id: Int,
        page: Int,
        list: StateLiveData<BasePagingResult<List<ArticleBean>>>
    ) {
        executeRequest({ navApi.projectListCid(page, id) }, list)
    }
}