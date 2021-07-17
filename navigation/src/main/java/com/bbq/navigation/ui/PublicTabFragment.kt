package com.bbq.navigation.ui

import android.view.View
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.alibaba.android.arouter.launcher.ARouter
import com.bbq.base.base.BaseVMFragment
import com.bbq.base.route.LoginServiceUtils
import com.bbq.base.route.WebService
import com.bbq.base.route.WebServiceUtils
import com.bbq.base.utils.showToast
import com.bbq.navigation.R
import com.bbq.navigation.adapter.NavTabRightAdapter
import com.bbq.navigation.adapter.PublicTabLeftAdapter
import com.bbq.navigation.adapter.SystemArticleAdapter
import com.bbq.navigation.bean.ArticleBean
import com.bbq.navigation.bean.PublicBean
import com.bbq.navigation.databinding.NavFragmentPublicTabBinding
import com.bbq.navigation.viewmodel.NavTabVM
import com.bbq.net.model.DataStatus
import org.koin.androidx.viewmodel.ext.android.viewModel

class PublicTabFragment : BaseVMFragment<NavFragmentPublicTabBinding>() {

    private val viewModel: NavTabVM by viewModel()

    private var mCurrentPosition = 0

    private val mLeftList by lazy {
        mutableListOf<PublicBean>()
    }

    private val mRightList by lazy {
        mutableListOf<ArticleBean>()
    }

    private val mLeftAdapter by lazy {
        PublicTabLeftAdapter(mLeftList)
    }

    private val mRightAdapter by lazy {
        SystemArticleAdapter(mRightList)
    }

    private lateinit var mCurrentNavTab: PublicBean

    override fun initView(view: View) {
        mBinding.recyclerLeft.layoutManager = LinearLayoutManager(requireContext())
        mBinding.recyclerLeft.adapter = mLeftAdapter

        mBinding.right.recyclerData.layoutManager = LinearLayoutManager(requireContext())
        mBinding.right.recyclerData.adapter = mRightAdapter

        mLeftAdapter.setOnItemClickListener { adapter, view, position ->
            if (mLeftList[position].id == mCurrentNavTab.id) return@setOnItemClickListener
            //选中当前的
            mLeftList.forEachIndexed { index, navTabBean ->
                if (index == position) {
                    navTabBean.isSelected = true
                    mCurrentNavTab = navTabBean
                    mCurrentPosition = 0
                    switchRightData()
                } else {
                    navTabBean.isSelected = false
                }
                mLeftAdapter.notifyDataSetChanged()
            }
        }

        initRightListener()
    }

    private fun initRightListener() {
        //下拉刷新和上拉加载更多
        mBinding.right.refreshLayout.setOnRefreshListener {
            mCurrentPosition = 0
            switchRightData()
        }
        mRightAdapter.loadMoreModule.setOnLoadMoreListener {
            mCurrentPosition++
            switchRightData()
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
        viewModel.getWeChatList()
    }

    override fun getLayoutId(): Int {
        return R.layout.nav_fragment_public_tab
    }

    override fun startObserver() {
        super.startObserver()
        viewModel.mWeChatList.observe(this, {
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
                    mLeftList.clear()
                    //默认选中第一个
                    it.data!![0].isSelected = true
                    mCurrentNavTab = it.data!![0]
                    //加入list
                    mLeftList.addAll(it.data!!)
                    mLeftAdapter.notifyDataSetChanged()
                    switchRightData()
                }
            }
        })

        viewModel.mWeChatDetailList.observe(this, {
            when (it.dataStatus) {
                DataStatus.STATE_LOADING -> {
                    if (mCurrentPosition == 0 && !mBinding.right.refreshLayout.isRefreshing) {
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
                                    mBinding.right.recyclerData
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
                                mBinding.right.recyclerData,
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
            if (!mBinding.right.refreshLayout.isRefreshing) {
                dismissLoading()
            } else {
                mBinding.right.refreshLayout.finishRefresh()
            }
        }
    }

    private fun switchRightData() {
        mRightAdapter.loadMoreModule.isEnableLoadMore = false
        viewModel.getWeChatDetailList(mCurrentNavTab.id, mCurrentPosition)
    }

}