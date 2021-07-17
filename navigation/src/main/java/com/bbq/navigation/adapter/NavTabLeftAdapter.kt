package com.bbq.navigation.adapter

import android.text.Html
import com.bbq.base.utils.getResColor
import com.bbq.navigation.R
import com.bbq.navigation.bean.NavTabBean
import com.bbq.navigation.databinding.NavItemNavLeftBinding
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseDataBindingHolder

class NavTabLeftAdapter(list: MutableList<NavTabBean>?) :
    BaseQuickAdapter<NavTabBean, BaseDataBindingHolder<NavItemNavLeftBinding>>(
        R.layout.nav_item_nav_left, list
    ) {
    override fun convert(holder: BaseDataBindingHolder<NavItemNavLeftBinding>, item: NavTabBean) {
        holder.dataBinding?.tvLeftTxt?.text = Html.fromHtml(item.name)
        val bgColor =
            if (item.isSelected) R.color.white.getResColor() else R.color.colorWhiteDark.getResColor()
        holder.dataBinding?.tvLeftTxt?.setBackgroundColor(bgColor)
    }
}