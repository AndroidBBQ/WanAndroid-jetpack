package com.bbq.navigation.viewmodel

import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import com.bbq.navigation.bean.ArticleBean

class ItemProject(val bean:ArticleBean) {
    var mTitle = ObservableField(bean.title)
    var mTime = ObservableField(bean.niceShareDate)
    var mDesc = ObservableField(bean.desc)
    var mAuthor = ObservableField(bean.author)
    var mCollect = ObservableBoolean(bean.collect)
}