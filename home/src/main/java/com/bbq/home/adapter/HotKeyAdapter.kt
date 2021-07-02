package com.bbq.home.adapter

import com.bbq.home.R
import com.bbq.home.bean.HotKeyBean
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder

class HotKeyAdapter(list: MutableList<HotKeyBean>?) :
    BaseQuickAdapter<HotKeyBean, BaseViewHolder>(R.layout.item_hot_key, list) {
    override fun convert(holder: BaseViewHolder, item: HotKeyBean) {
        holder.setText(R.id.hot_key, item.name)
    }
}