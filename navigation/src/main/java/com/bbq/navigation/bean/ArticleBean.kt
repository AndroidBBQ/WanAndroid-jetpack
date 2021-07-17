package com.bbq.navigation.bean

data class ArticleBean(
    val apkLink: String = "",
    val audit: String = "",
    val author: String = "",
    val canEdit: Boolean = false,
    val chapterId: String = "",
    val chapterName: String = "",
    var collect: Boolean = false,
    val courseId: String = "",
    val desc: String = "",
    val descMd: String = "",
    val envelopePic: String = "",
    var top: Boolean = false,
    val fresh: Boolean = false,
    val host: String = "",
    val id: Int,
    val link: String = "",
    val niceDate: String = "",
    val niceShareDate: String = "",
    val origin: String = "",
    val prefix: String = "",
    val projectLink: String = "",
    val publishTime: String = "",
    val realSuperChapterId: String = "",
    val selfVisible: String = "",
    val shareDate: String = "",
    val shareUser: String = "",
    val superChapterId: String = "",
    val superChapterName: String = "",
    var tags: List<ArticleTag>? = null,
    val title: String = "",
    val type: String = "",
    val userId: String = "",
    val visible: String = "",
    val zan: String = "",
    var isSelected: Boolean = false
)

data class ArticleTag(
    val name: String = "",
    val url: String = ""
)