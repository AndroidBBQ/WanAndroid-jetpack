package com.bbq.navigation.ui

import android.view.View
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.bbq.base.base.BaseVMFragment
import com.bbq.base.route.LoginServiceUtils
import com.bbq.base.route.WebServiceUtils
import com.bbq.navigation.R
import com.bbq.navigation.adapter.ProjectArticleAdapter
import com.bbq.navigation.bean.ArticleBean
import com.bbq.navigation.databinding.NavFragmentProjectTabBinding
import com.bbq.navigation.viewmodel.NavTabVM
import com.bbq.net.model.DataStatus
import org.koin.androidx.viewmodel.ext.android.viewModel

class ProjectTabFragment : BaseVMFragment<NavFragmentProjectTabBinding>() {
    private val viewModel: NavTabVM by viewModel()

    private var mCurrentPosition = 0
    private val mRightList by lazy {
        mutableListOf<ArticleBean>()
    }
    private val mRightAdapter by lazy {
        ProjectArticleAdapter(mRightList)
    }

    override fun initView(view: View) {
        mBinding.common.recyclerData.layoutManager = LinearLayoutManager(requireContext())
        mBinding.common.recyclerData.adapter = mRightAdapter

        //下拉刷新和上拉加载更多
        mBinding.common.refreshLayout.setOnRefreshListener {
            mCurrentPosition = 0
            getData()
        }
        mRightAdapter.loadMoreModule.setOnLoadMoreListener {
            mCurrentPosition++
            getData()
        }
        mRightAdapter.setOnItemClickListener { adapter, view, position ->
            val bean = mRightList.get(position)
            WebServiceUtils.goWeb(requireContext(), bean.title, bean.link!!, bean.id, bean.collect)
        }
        mRightAdapter.setOnItemChildClickListener { adapter, view, position ->
            if (view.id == R.id.tvCollect) {
                if (!LoginServiceUtils.isLogin()) {
                    //如果没有登录，先登录
                    LoginServiceUtils.start(requireContext())
                    return@setOnItemChildClickListener
                }
                val bean = mRightList.get(position)
                if (bean.collect) {
                    //取消收藏
                    lifecycleScope.launchWhenCreated {
                        val result = viewModel.unCollect(bean.id)
                        if (result) {
                            bean.collect = false
                            mRightAdapter.notifyItemChanged(position)
                            toast("取消收藏成功！")
                        } else {
                            toast("取消收藏失败!")
                        }
                    }
                } else {
                    //收藏
                    lifecycleScope.launchWhenCreated {
                        val result = viewModel.collect(bean.id)
                        if (result) {
                            bean.collect = true
                            mRightAdapter.notifyItemChanged(position)
                            toast("收藏成功！")
                        } else {
                            toast("收藏失败!")

                        }
                    }
                }
            }
        }
    }

    override fun initData() {
        super.initData()
        getData()
    }

    private fun getData() {
        mRightAdapter.loadMoreModule.isEnableLoadMore = false
        viewModel.getProjectList(mCurrentPosition)
    }

    override fun startObserver() {
        super.startObserver()
        viewModel.mProjectList.observe(this, {
            when (it.dataStatus) {
                DataStatus.STATE_LOADING -> {
                    if (mCurrentPosition == 0 && !mBinding.common.refreshLayout.isRefreshing) {
                        showLoading()
                    }
                }
                DataStatus.STATE_SUCCESS -> {
                    finishRefresh()
                    mRightAdapter.loadMoreModule.isEnableLoadMore = true
                    if (mCurrentPosition == 0) {
                        if (it.data?.datas.isNullOrEmpty()) {
                            //必须要先把数组设置为空
                            mRightAdapter.setNewInstance(mutableListOf())
                            //如果网络错误了
                            mRightAdapter.setEmptyView(
                                getMsgEmptyDataView(
                                    mBinding.common.recyclerData
                                )
                            )
                            return@observe
                        }
                        mRightList.clear()
                    }
                    mRightList.addAll(it.data?.datas!!)
                    mRightAdapter.notifyDataSetChanged()
                    //判断是否是最后一页
                    if (mCurrentPosition < it.data?.pageCount!!) {
                        mRightAdapter.loadMoreModule.loadMoreComplete()
                    } else {
                        mRightAdapter.loadMoreModule.loadMoreEnd()
                    }
                }
                DataStatus.STATE_ERROR -> {
                    finishRefresh()
                    if (mCurrentPosition == 0) {
                        //必须要先把数组设置为空
                        mRightAdapter.setNewInstance(mutableListOf())
                        //如果网络错误了
                        mRightAdapter.setEmptyView(
                            getMsgErrorView(
                                mBinding.common.recyclerData,
                                it.exception?.msg
                            )
                        )
                    }
                }
            }
        })
    }

    private fun finishRefresh() {
        if (mCurrentPosition == 0) {
            if (!mBinding.common.refreshLayout.isRefreshing) {
                dismissLoading()
            } else {
                mBinding.common.refreshLayout.finishRefresh()
            }
        }
    }

    override fun getLayoutId(): Int {
        return R.layout.nav_fragment_project_tab
    }
}