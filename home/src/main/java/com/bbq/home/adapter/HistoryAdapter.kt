package com.bbq.home.adapter

import com.bbq.home.R
import com.bbq.home.databinding.ItemHistorySearchBinding
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseDataBindingHolder

class HistoryAdapter(list: MutableList<String>?) :
    BaseQuickAdapter<String, BaseDataBindingHolder<ItemHistorySearchBinding>>(
        R.layout.item_history_search,
        list
    ) {
    override fun convert(holder: BaseDataBindingHolder<ItemHistorySearchBinding>, item: String) {
        holder.dataBinding?.title?.text = item
        addChildClickViewIds(R.id.delete)
    }

}