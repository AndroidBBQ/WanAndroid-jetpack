package com.bbq.home.ui

import android.util.Log
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bbq.base.base.BaseVMFragment
import com.bbq.base.utils.SimpleBannerHelper
import com.bbq.home.R
import com.bbq.home.adapter.HotKeyAdapter
import com.bbq.home.databinding.FragmentHomeBinding
import com.bbq.home.viewmodel.HomeViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel


class HomeFragment : BaseVMFragment<FragmentHomeBinding>() {
    private val TAG = "HomeFragment"

    private lateinit var bannerHelper: SimpleBannerHelper

    private val viewModel: HomeViewModel by viewModel()

    private val mHotKeyAdapter by lazy {
        HotKeyAdapter(viewModel.mHotKeyList.value)
    }

    override fun onResume() {
        super.onResume()
        bannerHelper.startTimerTask()
    }

    override fun onPause() {
        super.onPause()
        bannerHelper.stopTimerTask()
    }

    override fun getLayoutId(): Int {
        return R.layout.fragment_home
    }


    override fun initData() {
        super.initData()
        mBinding.vm = viewModel
        initTopSearch()
        initRecycler()
    }

    private fun initRecycler() {

    }


    private fun initTopSearch() {
        bannerHelper =
            SimpleBannerHelper(mBinding.recyclerHotKey, RecyclerView.VERTICAL)
        mBinding.viewBg.setOnClickListener {
            goSearch()
        }
        mBinding.ivAdd.setOnClickListener {
            goAdd()
        }
        mBinding.recyclerHotKey.layoutManager = LinearLayoutManager(context)
        mBinding.recyclerHotKey.adapter = mHotKeyAdapter
        viewModel.getHotKeys()
    }

    override fun startObserver() {
        super.startObserver()
        viewModel.mHotKeyList.observe(this, Observer {
            mHotKeyAdapter.setNewInstance(it)
            bannerHelper.startTimerTask()
        })
    }

    /**
     * 跳转到新增文章页面
     */
    private fun goAdd() {
        toast("add")
    }

    /**
     * 跳转到搜索页面
     */
    private fun goSearch() {
        Log.e(TAG, "goSearch: ${bannerHelper.findLastVisibleItemPosition()}")
        //当前展示的标题
//        val title = mHotKeyAdapter.getItem(bannerHelper.findLastVisibleItemPosition()).name
//        toast(title)
    }
}