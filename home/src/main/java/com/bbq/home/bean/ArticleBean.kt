package com.bbq.home.bean

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.bbq.home.db.TagTypeConverter

@Entity(tableName = "tab_article")
@TypeConverters(TagTypeConverter::class)
data class ArticleBean(
    var page: Int,
    var articleType: Int,
    val apkLink: String,
    val audit: Int,
    val author: String,
    val canEdit: Boolean,
    val chapterId: Int,
    val chapterName: String,
    var collect: Boolean,
    val courseId: Int,
    val desc: String,
    val descMd: String,
    val envelopePic: String,
    val fresh: Boolean,
    val host: String,
    @PrimaryKey
    val id: Int,
    val link: String?,
    val niceDate: String,
    val niceShareDate: String,
    val origin: String,
    val prefix: String,
    val projectLink: String,
    val publishTime: Long,
    val realSuperChapterId: Int,
    val selfVisible: Int,
    val shareDate: Long,
    val shareUser: String?,
    val superChapterId: Int?,
    val superChapterName: String?,
    var tags: MutableList<ArticleTag>?,
    val title: String,
    val type: Int?,
    val userId: Int?,
    val visible: Int?,
    val zan: Int?
)








