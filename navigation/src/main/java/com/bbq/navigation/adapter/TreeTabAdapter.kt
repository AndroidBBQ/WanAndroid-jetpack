package com.bbq.navigation.adapter

import android.view.LayoutInflater
import android.widget.TextView
import com.bbq.navigation.R
import com.bbq.navigation.bean.TreeBean
import com.bbq.navigation.databinding.NavItemTreeBinding
import com.bbq.navigation.ui.SystemListActivity
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseDataBindingHolder

class TreeTabAdapter(list: MutableList<TreeBean>) : BaseQuickAdapter<TreeBean,
        BaseDataBindingHolder<NavItemTreeBinding>>(
    R.layout.nav_item_tree, list
) {
    override fun convert(holder: BaseDataBindingHolder<NavItemTreeBinding>, item: TreeBean) {
        holder.dataBinding?.name?.text = item.name
        holder.dataBinding?.fbl?.removeAllViews()

        item.children?.forEachIndexed { index, treeBean ->
            val inflater = LayoutInflater.from(context)
            val tv: TextView =
                inflater.inflate(
                    R.layout.nav_fbl_system_children,
                    holder.dataBinding?.fbl,
                    false
                ) as TextView
            tv.text = treeBean.name
            tv.setOnClickListener {
                item.childrenSelectPosition = index
                SystemListActivity.launch(context, item)
            }
            holder.dataBinding?.fbl?.addView(tv)
        }
        holder.itemView.setOnClickListener {
            if (item.children.isNullOrEmpty()) return@setOnClickListener
            SystemListActivity.launch(context, item)
        }
    }
}