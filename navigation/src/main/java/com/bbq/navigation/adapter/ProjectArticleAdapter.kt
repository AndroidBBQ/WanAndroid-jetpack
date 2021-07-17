package com.bbq.navigation.adapter

import com.bbq.navigation.R
import com.bbq.navigation.bean.ArticleBean
import com.bbq.navigation.databinding.NavItemProjectBinding
import com.bbq.navigation.viewmodel.ItemProject
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.module.LoadMoreModule
import com.chad.library.adapter.base.viewholder.BaseDataBindingHolder

class ProjectArticleAdapter(list: MutableList<ArticleBean>?) :
    BaseQuickAdapter<ArticleBean, BaseDataBindingHolder<NavItemProjectBinding>>(
        R.layout.nav_item_project, list
    ), LoadMoreModule {
    override fun convert(
        holder: BaseDataBindingHolder<NavItemProjectBinding>,
        item: ArticleBean
    ) {
        val binding = holder.dataBinding
        val article = ItemProject(item)
        binding?.item = article
        Glide.with(context)
            .applyDefaultRequestOptions(
                RequestOptions()
                .placeholder(R.drawable.nav_place_holder)
                .centerCrop())
            .load(item.envelopePic).into(binding?.iv!!)
        addChildClickViewIds(R.id.tvCollect)
    }
}