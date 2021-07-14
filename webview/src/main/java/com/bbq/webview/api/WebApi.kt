package com.bbq.webview.api

import com.bbq.net.model.BaseResult
import retrofit2.http.POST
import retrofit2.http.Path

interface WebApi {
    //收藏站内文章 文章id，拼接在链接中。
    @POST("/lg/collect/{id}/json")
    suspend fun collect(@Path("id") id: Int?): BaseResult<String>

    //取消收藏  文章列表 文章id，拼接在链接中。
    @POST("lg/uncollect_originId/{id}/json")
    suspend fun unCollect(@Path("id") id: Int?): BaseResult<String>
}