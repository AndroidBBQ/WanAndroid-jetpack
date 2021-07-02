package com.bbq.home.repo

import com.bbq.base.base.BaseRepository
import com.bbq.home.api.HomeApi
import com.bbq.home.bean.ArticleBean
import com.bbq.home.bean.ArticleTag
import com.bbq.home.bean.BannerBean
import com.bbq.home.bean.HotKeyBean
import com.bbq.net.model.BasePagingResult
import com.bbq.net.model.BaseResult
import com.bbq.net.model.ResultState

class HomeRepo(private val homeApi: HomeApi) :
    BaseRepository() {


    suspend fun getHotKey(): ResultState<MutableList<HotKeyBean>> {
        return callRequest(call = { handleResponse(homeApi.getHotKey()) })
    }

    suspend fun getBanners(): ResultState<MutableList<BannerBean>> {
        return callRequest { handleResponse(homeApi.getBanner()) }
    }

    suspend fun getArticleList(page: Int): ResultState<BasePagingResult<MutableList<ArticleBean>>> {
        //如果page==0的话，不仅要获取文章还有获取置顶的
        return if (page == 0) {
            val topResult = homeApi.articleTop()
            topResult.data.forEach {
                it.tags?.add(ArticleTag("置顶"))
                it.page = -1
            }
            val articleResult = homeApi.getHomeList(page)
            articleResult.data.datas.forEach {
                it.page = page
            }
            articleResult.data.datas.addAll(0, topResult.data)
            callRequest { handleResponse(articleResult) }
        } else {
            val articleResult = homeApi.getHomeList(page)
            articleResult.data.datas.forEach {
                it.page = page
            }
            callRequest { handleResponse(articleResult) }
        }
    }

    //搜索
    suspend fun searchArticles(
        page: Int,
        key: String
    ): ResultState<BasePagingResult<MutableList<ArticleBean>>> {
        return callRequest { handleResponse(homeApi.search(page, key)) }
    }
}