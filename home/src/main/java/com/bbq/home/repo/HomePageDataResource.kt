package com.bbq.home.repo

import androidx.paging.PagingSource
import androidx.paging.PagingState
import androidx.room.withTransaction
import com.bbq.home.bean.ArticleBean
import com.bbq.home.db.HomeDatabase
import com.bbq.net.model.ResultState

class HomePageDataResource(val repo: HomeRepo, val database: HomeDatabase) :
    PagingSource<Int, ArticleBean>() {
    //params ：请求列表需要的参数
    //LoadResult ：列表数据请求结果，包含下一页要请求的key
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, ArticleBean> {
        //从start开始
        val currentPage = params.key ?: 0
        //从repo中获取数据，repo不管是本地的还是网络的
        when (val response = repo.getArticleList(currentPage)) {
            is ResultState.Success -> {
                //当前页码 小于 总页码 页面加1
                var nextPage = if (currentPage < response.data!!.pageCount) {
                    currentPage + 1
                } else {
                    //没有更多数据
                    null
                }
                //将数据保存到数据库中
                database.articleDao().clearArticleByPage(currentPage)
                database.articleDao().insertArticle(response.data!!.datas)
//                data ：返回的数据列表
//                prevKey ：上一页的key （传 null 表示没有上一页）
//                nextKey ：下一页的key （传 null 表示没有下一页）
                return LoadResult.Page(
                    data = response.data!!.datas,
                    prevKey = null,
                    nextKey = nextPage
                )
            }
            is ResultState.Error -> {
                //从数据库中取
//                return LoadResult.Error(response.exception)
                val dbData = database.withTransaction {
                    if (currentPage == 0) {
                        val topData = database.articleDao().queryLocalArticle(-1)
                        val article = database.articleDao().queryLocalArticle(currentPage)
                        val tmpData = mutableListOf<ArticleBean>()
                        tmpData.addAll(topData)
                        tmpData.addAll(article)
                        tmpData
                    } else {
                        database.articleDao().queryLocalArticle(currentPage)
                    }
                }
                return if (dbData.isEmpty()) {
                    LoadResult.Error(response.exception)
                } else {
                    LoadResult.Page(
                        data = dbData,
                        prevKey = null,
                        nextKey = null
                    )
                }
            }
            else -> {
                //理论上不会走到这里的
                return LoadResult.Error(Exception())
            }
        }
    }

    override fun getRefreshKey(state: PagingState<Int, ArticleBean>): Int? {
        return 0
    }
}