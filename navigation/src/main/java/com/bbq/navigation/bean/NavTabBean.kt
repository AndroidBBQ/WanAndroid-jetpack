package com.bbq.navigation.bean

data class NavTabBean(
    val articles: List<ArticleBean>? = null,
    val cid: String = "",
    val name: String = "",
    var isSelected: Boolean = false
)