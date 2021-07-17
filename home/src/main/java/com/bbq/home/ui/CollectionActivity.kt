package com.bbq.home.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.alibaba.android.arouter.launcher.ARouter
import com.bbq.base.base.BaseVMActivity
import com.bbq.base.bean.EventCollectBean
import com.bbq.base.route.LoginServiceUtils
import com.bbq.base.route.WebService
import com.bbq.base.utils.LiveDataBus
import com.bbq.base.utils.StatusBarUtil
import com.bbq.base.utils.getResColor
import com.bbq.home.R
import com.bbq.home.adapter.HomeArticleAdapter
import com.bbq.home.bean.ArticleBean
import com.bbq.home.databinding.FragmentFaqBinding
import com.bbq.home.viewmodel.FaqVM
import com.bbq.net.model.DataStatus
import org.koin.androidx.viewmodel.ext.android.viewModel

class CollectionActivity : BaseVMActivity<FragmentFaqBinding>() {
    private val viewModel: FaqVM by viewModel()

    private val mArticleList by lazy {
        mutableListOf<ArticleBean>()
    }

    private val mArticleAdapter by lazy {
        HomeArticleAdapter(mArticleList)
    }

    override fun initView(savedInstanceState: Bundle?) {
        StatusBarUtil.setColor(this, R.color.theme.getResColor())
        mBinding.vm = viewModel
        viewModel.setTitle("我的收藏")
        viewModel.setLeftIcon()
        initRecycler()
        initRecyclerListener()
        LiveDataBus.with<EventCollectBean>("EventCollectBean")
            .observe(this, {
                mArticleList.forEachIndexed { index, articleBean ->
                    if (articleBean.id == it.articleId) {
                        articleBean.collect = it.isCollect
                        mArticleAdapter.notifyItemChanged(index)
                    }
                }
            })
    }

    private fun initRecyclerListener() {
        //下拉刷新和上拉加载更多
        mBinding.smartRefresh.setOnRefreshListener {
            mCurrentPosition = 0
            getData()
        }
        mArticleAdapter.loadMoreModule.setOnLoadMoreListener {
            mCurrentPosition++
            getData()
        }
        mArticleAdapter.setOnItemClickListener { adapter, view, position ->
            val bean = mArticleAdapter.getItem(position)
            ARouter.getInstance().navigation(WebService::class.java)
                .goWeb(this, bean.title, bean.link!!, bean.id, bean.collect)
        }
        mArticleAdapter.setOnItemChildClickListener { adapter, view, position ->
            if (view.id == R.id.tvCollect) {
                if (!LoginServiceUtils.isLogin()) {
                    //如果没有登录，先登录
                    LoginServiceUtils.start(this)
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

    private var mCurrentPosition = 0

    override fun initData() {
        super.initData()
        getData()
    }

    private fun getData() {
        // 这里的作用是防止下拉刷新的时候还可以上拉加载,请求成功后要设置为true
        mArticleAdapter.loadMoreModule.isEnableLoadMore = false
        viewModel.getCollectList(mCurrentPosition)
    }

    override fun startObserver() {
        super.startObserver()
        viewModel.mCollectionList.observe(this, {
            when (it.dataStatus) {
                DataStatus.STATE_LOADING -> {
                    if (mCurrentPosition == 0 && !mBinding.smartRefresh.isRefreshing) {
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
                                    mBinding.recyclerData
                                )
                            )
                            return@observe
                        }
                        mArticleList.clear()
                    }
                    it.data?.datas?.forEach { that ->
                        that.collect = true
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
                                mBinding.recyclerData,
                                it.exception?.msg
                            )
                        )
                    }
                }
            }
        })

        viewModel.mFinish.observe(this, {
            if (it) {
                finish()
            }
        })
    }

    private fun finishRefresh() {
        if (mCurrentPosition == 0) {
            if (!mBinding.smartRefresh.isRefreshing) {
                dismissLoading()
            } else {
                mBinding.smartRefresh.finishRefresh()
            }
        }
    }

    private fun initRecycler() {
        mBinding.recyclerData.layoutManager = LinearLayoutManager(this)
        mBinding.recyclerData.adapter = mArticleAdapter
    }

    override fun getLayoutId(): Int {
        return R.layout.fragment_faq
    }

    companion object {
        fun launch(context: Context) {
            val intent = Intent(context, CollectionActivity::class.java)
            context.startActivity(intent)
        }
    }
}