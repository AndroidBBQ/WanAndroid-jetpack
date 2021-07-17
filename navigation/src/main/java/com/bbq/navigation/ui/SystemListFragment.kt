package com.bbq.navigation.ui

import android.os.Bundle
import android.view.View
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.bbq.base.base.BaseVMFragment
import com.bbq.base.route.LoginServiceUtils
import com.bbq.base.route.WebServiceUtils
import com.bbq.base.utils.StatusBarUtil
import com.bbq.navigation.R
import com.bbq.navigation.adapter.SystemArticleAdapter
import com.bbq.navigation.bean.ArticleBean
import com.bbq.navigation.bean.TreeBean
import com.bbq.navigation.databinding.NavFragmentSystemListBinding
import com.bbq.navigation.viewmodel.SystemListVM
import com.bbq.net.model.DataStatus
import org.koin.androidx.viewmodel.ext.android.viewModel

class SystemListFragment : BaseVMFragment<NavFragmentSystemListBinding>() {

    private val mBean: TreeBean by lazy {
        arguments?.getParcelable("key_bean")!!
    }
    private val viewModel: SystemListVM by viewModel()

    private val mArticleList by lazy {
        mutableListOf<ArticleBean>()
    }

    private val mArticleAdapter by lazy {
        SystemArticleAdapter(mArticleList)
    }

    override fun initView(view: View) {
        initRecycler()
        initRecyclerListener()
    }

    private fun initRecycler() {
        mBinding.comRefresh.recyclerData.layoutManager = LinearLayoutManager(requireContext())
        mBinding.comRefresh.recyclerData.adapter = mArticleAdapter
    }

    private fun initRecyclerListener() {
        //下拉刷新和上拉加载更多
        mBinding.comRefresh.refreshLayout.setOnRefreshListener {
            mCurrentPosition = 0
            getData()
        }
        mArticleAdapter.loadMoreModule.setOnLoadMoreListener {
            mCurrentPosition++
            getData()
        }
        mArticleAdapter.setOnItemClickListener { adapter, view, position ->
            val bean = mArticleAdapter.getItem(position)
            WebServiceUtils.goWeb(requireContext(), bean.title, bean.link!!, bean.id, bean.collect)
        }
        mArticleAdapter.setOnItemChildClickListener { adapter, view, position ->
            if (view.id == R.id.tvCollect) {
                if (!LoginServiceUtils.isLogin()) {
                    //如果没有登录，先登录
                    LoginServiceUtils.start(requireContext())
                    return@setOnItemChildClickListener
                }
                val bean = mArticleAdapter.getItem(position)
                if (bean.collect) {
                    //取消收藏
                    lifecycleScope.launchWhenCreated {
                        val result = viewModel.unCollect(bean.id)
                        if (result) {
                            bean.collect = false
                            mArticleAdapter.notifyItemChanged(position)
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
                            mArticleAdapter.notifyItemChanged(position)
                            toast("收藏成功！")
                        } else {
                            toast("收藏失败!")

                        }
                    }
                }
            }
        }
    }

    override fun getLayoutId(): Int {
        return R.layout.nav_fragment_system_list
    }

    private var mCurrentPosition = 0

    override fun initData() {
        super.initData()
        getData()
    }

    private fun getData() {
        // 这里的作用是防止下拉刷新的时候还可以上拉加载,请求成功后要设置为true
        mArticleAdapter.loadMoreModule.isEnableLoadMore = false
        viewModel.articleList(mCurrentPosition, mBean.id)
    }


    override fun startObserver() {
        super.startObserver()
        viewModel.mArticleList.observe(this, {
            when (it.dataStatus) {
                DataStatus.STATE_LOADING -> {
                    if (mCurrentPosition == 0 && !mBinding.comRefresh.refreshLayout.isRefreshing) {
                        showLoading()
                    }
                }
                DataStatus.STATE_SUCCESS -> {
                    finishRefresh()
                    mArticleAdapter.loadMoreModule.isEnableLoadMore = true
                    if (mCurrentPosition == 0) {
                        if (it.data?.datas.isNullOrEmpty()) {
                            //必须要先把数组设置为空
                            mArticleAdapter.setNewInstance(mutableListOf())
                            //如果网络错误了
                            mArticleAdapter.setEmptyView(
                                getMsgEmptyDataView(
                                    mBinding.comRefresh.recyclerData
                                )
                            )
                            return@observe
                        }
                        mArticleList.clear()
                    }
                    mArticleList.addAll(it.data?.datas!!)
                    mArticleAdapter.notifyDataSetChanged()
                    //判断是否是最后一页
                    if (mCurrentPosition < it.data?.pageCount!!) {
                        mArticleAdapter.loadMoreModule.loadMoreComplete()
                    } else {
                        mArticleAdapter.loadMoreModule.loadMoreEnd()
                    }
                }
                DataStatus.STATE_ERROR -> {
                    finishRefresh()
                    if (mCurrentPosition == 0) {
                        //必须要先把数组设置为空
                        mArticleAdapter.setNewInstance(mutableListOf())
                        //如果网络错误了
                        mArticleAdapter.setEmptyView(
                            getMsgErrorView(
                                mBinding.comRefresh.recyclerData,
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
            if (!mBinding.comRefresh.refreshLayout.isRefreshing) {
                dismissLoading()
            } else {
                mBinding.comRefresh.refreshLayout.finishRefresh()
            }
        }
    }

    companion object {
        fun newInstance(bean: TreeBean): SystemListFragment {
            val fragment = SystemListFragment()
            val bundle = Bundle()
            bundle.putParcelable("key_bean", bean)
            fragment.arguments = bundle
            return fragment
        }
    }
}