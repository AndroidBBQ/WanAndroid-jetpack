package com.bbq.navigation.adapter

import com.bbq.navigation.R
import com.bbq.navigation.bean.ArticleBean
import com.bbq.navigation.databinding.NavItemSystemListBinding
import com.bbq.navigation.viewmodel.ItemHomeArticle
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.module.LoadMoreModule
import com.chad.library.adapter.base.viewholder.BaseDataBindingHolder

class SystemArticleAdapter(list: MutableList<ArticleBean>?) :
    BaseQuickAdapter<ArticleBean, BaseDataBindingHolder<NavItemSystemListBinding>>(
        R.layout.nav_item_system_list, list
    ), LoadMoreModule {
    override fun convert(
        holder: BaseDataBindingHolder<NavItemSystemListBinding>,
        item: ArticleBean
    ) {
        val binding = holder.dataBinding
        val article = ItemHomeArticle(item)
        article.bindData()
        binding?.item = article
        addChildClickViewIds(R.id.tvCollect)
    }
}