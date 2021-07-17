package com.bbq.navigation.ui

import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.bbq.base.base.BaseVMFragment
import com.bbq.base.utils.showToast
import com.bbq.navigation.R
import com.bbq.navigation.adapter.TreeTabAdapter
import com.bbq.navigation.bean.TreeBean
import com.bbq.navigation.databinding.NavFragmentSystemTabBinding
import com.bbq.navigation.viewmodel.NavTabVM
import com.bbq.net.model.DataStatus
import org.koin.androidx.viewmodel.ext.android.viewModel

class SystemTabFragment : BaseVMFragment<NavFragmentSystemTabBinding>() {
    private val viewModel: NavTabVM by viewModel()
    private val mDataList by lazy {
        mutableListOf<TreeBean>()
    }
    private val mTreeTabAdapter by lazy {
        TreeTabAdapter(mDataList)
    }

    override fun initView(view: View) {
        mBinding.recyclerData.layoutManager = LinearLayoutManager(requireContext())
        mBinding.recyclerData.adapter = mTreeTabAdapter
    }

    override fun initData() {
        super.initData()
        viewModel.getTreeList()
    }

    override fun startObserver() {
        super.startObserver()
        viewModel.mTreeList.observe(this, {
            when (it.dataStatus) {
                DataStatus.STATE_LOADING -> {
                    showLoading()
                }
                DataStatus.STATE_ERROR -> {
                    dismissLoading()
                    it?.exception?.msg?.showToast()
                }
                DataStatus.STATE_SUCCESS -> {
                    dismissLoading()
                    if (it.data.isNullOrEmpty()) {
                        toast("数据为空!")
                        return@observe
                    }
                    mDataList.clear()
                    mDataList.addAll(it.data!!)
                    mTreeTabAdapter.notifyDataSetChanged()
                }
            }
        })
    }

    override fun getLayoutId(): Int {
        return R.layout.nav_fragment_system_tab
    }
}