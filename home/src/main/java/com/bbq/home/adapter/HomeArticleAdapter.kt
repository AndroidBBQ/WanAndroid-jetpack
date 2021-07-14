package com.bbq.home.adapter

import com.bbq.home.R
import com.bbq.home.bean.ArticleBean
import com.bbq.home.databinding.ItemHomeBinding
import com.bbq.home.databinding.ItemHomeSearchBinding
import com.bbq.home.viewmodel.ItemHomeArticle
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.module.LoadMoreModule
import com.chad.library.adapter.base.viewholder.BaseDataBindingHolder

class HomeArticleAdapter(list: MutableList<ArticleBean>?) :
    BaseQuickAdapter<ArticleBean, BaseDataBindingHolder<ItemHomeSearchBinding>>(
        R.layout.item_home_search, list
    ), LoadMoreModule {
    override fun convert(holder: BaseDataBindingHolder<ItemHomeSearchBinding>, item: ArticleBean) {
        val binding = holder.dataBinding
        val article = ItemHomeArticle(item)
        article.bindData()
        binding?.item = article
        addChildClickViewIds(R.id.tvCollect)
    }
}