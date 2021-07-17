package com.bbq.home.api

import com.bbq.home.bean.ArticleBean
import com.bbq.home.bean.BannerBean
import com.bbq.home.bean.HotKeyBean
import com.bbq.net.model.BasePagingResult
import com.bbq.net.model.BaseResult
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface HomeApi {
    @GET("/hotkey/json")
    suspend fun getHotKey(): BaseResult<MutableList<HotKeyBean>>

    @GET("/banner/json")
    suspend fun getBanner(): BaseResult<MutableList<BannerBean>>

    //文章
    @GET("/article/list/{count}/json")
    suspend fun getHomeList(@Path("count") count: Int): BaseResult<BasePagingResult<MutableList<ArticleBean>>>


    //置顶文章
    @GET("/article/top/json")
    suspend fun articleTop(): BaseResult<MutableList<ArticleBean>>


    @POST("/article/query/{page}/json")
    suspend fun search(
        @Path("page") page: Int,
        @Query("k") key: String
    ): BaseResult<BasePagingResult<MutableList<ArticleBean>>>


    //收藏站内文章 文章id，拼接在链接中。
    @POST("/lg/collect/{id}/json")
    suspend fun collect(@Path("id") id: Int?): BaseResult<String>

    //取消收藏  文章列表 文章id，拼接在链接中。
    @POST("lg/uncollect_originId/{id}/json")
    suspend fun unCollect(@Path("id") id: Int?): BaseResult<String>

    //获取问答列表
    @GET("wenda/list/{page}/json ")
    suspend fun wendaList(@Path("page") page: Int): BaseResult<BasePagingResult<List<ArticleBean>>>


    //自己收藏文章列表
    @GET("lg/collect/list/{page}/json")
    suspend fun lgCollectList(@Path("page") page: Int): BaseResult<BasePagingResult<List<ArticleBean>>>
}