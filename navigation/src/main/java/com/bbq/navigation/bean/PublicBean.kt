package com.bbq.navigation.bean

data class PublicBean(
    val children: List<Any?>? = null,
    val courseId: Int? = null,
    val id: Int,
    val name: String? = null,
    val order: Int? = null,
    val parentChapterId: Int? = null,
    val userControlSetTop: Boolean? = null,
    val visible: Int? = null,
    var isSelected: Boolean = false
)