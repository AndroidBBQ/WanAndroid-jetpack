package com.bbq.home.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.bbq.home.bean.ArticleBean

@Dao
interface ArticleDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertArticle(articleDataList: List<ArticleBean>)

    @Query("SELECT * FROM tab_article where page=:page")
    fun queryLocalArticle(page: Int): List<ArticleBean>

    @Query("DELETE FROM tab_article where page=:page")
    suspend fun clearArticleByPage(page: Int)
}