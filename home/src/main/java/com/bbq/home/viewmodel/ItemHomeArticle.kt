package com.bbq.home.viewmodel

import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import androidx.lifecycle.MutableLiveData
import com.bbq.base.utils.truely
import com.bbq.home.bean.ArticleBean
import com.bbq.home.bean.ArticleTag
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ItemHomeArticle(val bean: ArticleBean) {

    var mTitle = ObservableField("")
    var mTime = ObservableField("")
    var mAuthor = ObservableField("")
    var mCategory = ObservableField("")
    var mLink: String? = ""
    var mId: Int? = null
    var mCollect = ObservableBoolean()
        set(value) {
            bean.collect = value.get()
            field = value
        }
    var mCollectIconShow = ObservableBoolean(true)

    var mTagVM1 = ObservableField<TagViewModel>()
    var mTagVM2 = ObservableField<TagViewModel>()
    var mTagVM3 = ObservableField<TagViewModel>()
    var mTagVM4 = ObservableField<TagViewModel>()


    fun bindData() {
        setTitle(bean)
        setTime(bean)
        setAuthor(bean)
        setCategory(bean)
        setTags(bean)
        mId = bean.id
        mLink = bean.link
        mCollect.set(bean.collect)
    }

    private fun addNewTags(bean: ArticleBean?) {
        val tempTags = mutableListOf<ArticleTag>()
        bean?.tags?.let { tag -> tempTags.addAll(tag) }
        if (bean?.fresh.truely()) {
            tempTags.add(ArticleTag("新"))
        }
        bean?.tags = tempTags
    }

    private fun setTags(bean: ArticleBean) {
        addNewTags(bean)
        if (!bean?.tags.isNullOrEmpty()) {
            val tags = bean?.tags!!
            when (tags.size) {
                4 -> {
                    mTagVM1.set(TagViewModel().apply {
                        mContent.set(tags[0].name)
                    })
                    mTagVM2.set(TagViewModel().apply {
                        mContent.set(tags[1].name)
                    })
                    mTagVM3.set(TagViewModel().apply {
                        mContent.set(tags[2].name)
                    })
                    mTagVM4.set(TagViewModel().apply {
                        mContent.set(tags[3].name)
                    })
                }
                3 -> {
                    mTagVM2.set(TagViewModel().apply {
                        mContent.set(tags[0].name)
                    })
                    mTagVM3.set(TagViewModel().apply {
                        mContent.set(tags[1].name)
                    })
                    mTagVM4.set(TagViewModel().apply {
                        mContent.set(tags[2].name)
                    })
                }
                2 -> {
                    mTagVM3.set(TagViewModel().apply {
                        mContent.set(tags[0].name)
                    })
                    mTagVM4.set(TagViewModel().apply {
                        mContent.set(tags[1].name)
                    })
                }
                1 -> {
                    mTagVM4.set(TagViewModel().apply {
                        mContent.set(tags[0].name)
                    })
                }
            }
        }

    }

    fun setTitle(bean: ArticleBean) {
        mTitle.set(bean?.title)
    }

    fun setTime(bean: ArticleBean) {
        mTime.set(bean?.niceDate)
    }

    fun setAuthor(bean: ArticleBean) {
        if (!bean?.author.isNullOrEmpty()) {
            mAuthor.set("作者: ${bean?.author}")
        } else if (!bean?.shareUser.isNullOrEmpty()) {
            mAuthor.set("分享人: ${bean?.shareUser}")
        }
    }

    fun setCategory(bean: ArticleBean) {
        bean?.superChapterName?.let {
            mCategory.set("分类: $it")
        }
    }
}