package com.bbq.home.api

import com.bbq.home.bean.ArticleBean
import com.bbq.home.bean.BannerBean
import com.bbq.home.bean.HotKeyBean
import com.bbq.net.model.BasePagingResult
import com.bbq.net.model.BaseResult
import retrofit2.http.GET
import retrofit2.http.Path

interface HomeApi {
    @GET("/hotkey/json")
    suspend fun getHotKey(): BaseResult<MutableList<HotKeyBean>>

    @GET("/banner/json")
    suspend fun getBanner(): BaseResult<MutableList<BannerBean>>

    @GET("/article/list/{count}/json")
    suspend fun getHomeList(@Path("count") count: Int): BaseResult<BasePagingResult<MutableList<ArticleBean>>>
}