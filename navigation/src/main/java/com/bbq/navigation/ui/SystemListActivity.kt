package com.bbq.navigation.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.bbq.base.base.BaseVMActivity
import com.bbq.navigation.R
import com.bbq.navigation.bean.TreeBean
import com.bbq.navigation.databinding.NavActivitySystemListBinding
import com.bbq.navigation.viewmodel.SystemListVM
import com.google.android.material.tabs.TabLayoutMediator
import org.koin.androidx.viewmodel.ext.android.viewModel

class SystemListActivity : BaseVMActivity<NavActivitySystemListBinding>() {
    private val viewModel: SystemListVM by viewModel()
    private val mTreeBean: TreeBean by lazy {
        intent.getParcelableExtra("key_bean")!!
    }
    private val mFragmentList = mutableListOf<Fragment>()
    private val mTabList = mutableListOf<String>()

    override fun initView(savedInstanceState: Bundle?) {
        mBinding.vm = viewModel
        viewModel.setTitle(mTreeBean.name!!)
        initTab()
    }

    private fun initTab() {
        mFragmentList.clear()
        mTabList.clear()
        mTreeBean.children!!.forEachIndexed { index, treeBean ->
            mBinding.tabLayout.addTab(mBinding.tabLayout.newTab().apply {
                setCustomView(R.layout.nav_item_tab_paper)
                customView!!.findViewById<TextView>(R.id.tv_tab_name).text = treeBean.name
            })
            val fragment = SystemListFragment.newInstance(treeBean)
            mFragmentList.add(fragment)
            mTabList.add(treeBean.name!!)
        }
        mBinding.viewPager.offscreenPageLimit = mFragmentList.size
        mBinding.viewPager.adapter =
            object : FragmentStateAdapter(supportFragmentManager, lifecycle) {
                override fun getItemCount(): Int {
                    return mFragmentList.size
                }

                override fun createFragment(position: Int): Fragment {
                    return mFragmentList[position]
                }
            }
        TabLayoutMediator(mBinding.tabLayout, mBinding.viewPager) { tab, position ->
            tab.text = mTabList[position]
        }.attach()
        mBinding.viewPager.currentItem = mTreeBean.childrenSelectPosition
    }

    override fun startObserver() {
        super.startObserver()
        viewModel.mIsFinish.observe(this, {
            if (it) finish()
        })
    }

    override fun getLayoutId(): Int {
        return R.layout.nav_activity_system_list
    }


    companion object {
        fun launch(context: Context, treeBean: TreeBean) {
            val intent = Intent(context, SystemListActivity::class.java)
            intent.putExtra("key_bean", treeBean)
            context.startActivity(intent)
        }
    }
}