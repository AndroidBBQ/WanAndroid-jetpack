package com.bbq.navigation.api

import com.bbq.navigation.bean.ArticleBean
import com.bbq.navigation.bean.NavTabBean
import com.bbq.navigation.bean.PublicBean
import com.bbq.navigation.bean.TreeBean
import com.bbq.net.model.BasePagingResult
import com.bbq.net.model.BaseResult
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface NavApi {
    //导航数据
    @GET("navi/json")
    suspend fun naviList(): BaseResult<List<NavTabBean>>

    //体系数据
    @GET("tree/json")
    suspend fun treeList(): BaseResult<List<TreeBean>>

    /**
     *  1.首页文章列表
     *  2.知识体系下的文章  cid 分类的id，上述二级目录的id
     */
    @GET("article/list/{page}/json")
    suspend fun articleList(
        @Path("page") page: Int,
        @Query("cid") cid: Int? = null
    ): BaseResult<BasePagingResult<List<ArticleBean>>>


    //收藏站内文章 文章id，拼接在链接中。
    @POST("lg/collect/{id}/json")
    suspend fun collect(@Path("id") id: Int?): BaseResult<String>

    //取消收藏  文章列表 文章id，拼接在链接中。
    @POST("lg/uncollect_originId/{id}/json")
    suspend fun unCollect(@Path("id") id: Int?): BaseResult<String>


    //获取公众号列表
    @GET("wxarticle/chapters/json")
    suspend fun weChatList(): BaseResult<List<PublicBean>>

    //查看某个公众号历史数据
    @GET("wxarticle/list/{id}/{page}/json")
    suspend fun weChatListDetail(
        @Path("id") id: Int?,
        @Path("page") page: Int
    ): BaseResult<BasePagingResult<List<ArticleBean>>>


    //项目(热门项目)
    @GET("article/listproject/{page}/json")
    suspend fun projectList(@Path("page") page: Int): BaseResult<BasePagingResult<List<ArticleBean>>>

    //项目分类
    @GET("project/tree/json")
    suspend fun projectTreeList(): BaseResult<List<PublicBean>>

    //项目分类 从1开始
    @GET("project/list/{page}/json")
    suspend fun projectListCid(
        @Path("page") page: Int,
        @Query("cid") cid: Int? = null
    ): BaseResult<BasePagingResult<List<ArticleBean>>>
}