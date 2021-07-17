package com.bbq.navigation.adapter

import com.bbq.base.utils.getResColor
import com.bbq.navigation.R
import com.bbq.navigation.bean.NavTabBean
import com.bbq.navigation.bean.PublicBean
import com.bbq.navigation.databinding.NavItemNavLeftBinding
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseDataBindingHolder

class PublicTabLeftAdapter(list: MutableList<PublicBean>?) :
    BaseQuickAdapter<PublicBean, BaseDataBindingHolder<NavItemNavLeftBinding>>(
        R.layout.nav_item_nav_left, list
    ) {
    override fun convert(holder: BaseDataBindingHolder<NavItemNavLeftBinding>, item: PublicBean) {
        holder.dataBinding?.tvLeftTxt?.text = item.name
        val bgColor =
            if (item.isSelected) R.color.white.getResColor() else R.color.colorWhiteDark.getResColor()
        holder.dataBinding?.tvLeftTxt?.setBackgroundColor(bgColor)
    }
}