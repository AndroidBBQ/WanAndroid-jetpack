package com.bbq.home.ui

import android.content.Intent
import android.view.View
import android.widget.AdapterView
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.alibaba.android.arouter.launcher.ARouter
import com.bbq.base.base.BaseVMFragment
import com.bbq.base.bean.EventCollectBean
import com.bbq.base.route.LoginServiceUtils
import com.bbq.base.route.WebService
import com.bbq.base.utils.LiveDataBus
import com.bbq.base.utils.SimpleBannerHelper
import com.bbq.base.utils.getResColor
import com.bbq.home.R
import com.bbq.home.adapter.HomePageAdapter
import com.bbq.home.adapter.HotKeyAdapter
import com.bbq.home.bean.ArticleBean
import com.bbq.home.databinding.FragmentHomeBinding
import com.bbq.home.viewmodel.HomeViewModel
import com.bbq.home.viewmodel.ItemHomeArticle
import com.bbq.home.weight.FooterAdapter
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.listener.OnItemClickListener
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.koin.androidx.viewmodel.ext.android.viewModel


class HomeFragment : BaseVMFragment<FragmentHomeBinding>() {
    private val TAG = "HomeFragment"

    private lateinit var bannerHelper: SimpleBannerHelper


    private val viewModel: HomeViewModel by viewModel()

    private val mHotKeyAdapter by lazy {
        HotKeyAdapter(viewModel.mHotKeyList.value)
    }

    private val mArticleAdapter by lazy {
        HomePageAdapter(requireContext())
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
        viewModel.dialogState(this)
        initTopSearch()
        initRecycler()
        initSwipeRefresh()
        viewModel.getHotKeys()
        viewModel.getBannerList()
        lifecycleScope.launch(Dispatchers.IO) {
            viewModel.getArticles().collectLatest {
                withContext(Dispatchers.Main) {
                    mArticleAdapter.submitData(it)
                }
            }
        }
        initListener()
    }

    private fun initListener() {
        mArticleAdapter.setOnCollectListener(object : HomePageAdapter.OnCollectListener {
            override fun collect(articleBean: ItemHomeArticle, position: Int) {
                if (!LoginServiceUtils.isLogin()) {
                    //如果没有登录，先登录
                    LoginServiceUtils.start(requireContext())
                    return
                }
                if (articleBean.mCollect.get()) {
                    //取消收藏
                    lifecycleScope.launchWhenCreated {
                        val result = viewModel.unCollect(articleBean.mId)
                        if (result) {
                            articleBean.mCollect.set(false)
                            toast("取消收藏成功！")
                        } else {
                            toast("取消收藏失败!")
                        }
                    }
                } else {
                    //收藏
                    lifecycleScope.launchWhenCreated {
                        val result = viewModel.collect(articleBean.mId)
                        if (result) {
                            articleBean.mCollect.set(true)
                            toast("收藏成功！")
                        } else {
                            toast("收藏失败!")

                        }
                    }
                }
            }
        })
        LiveDataBus.with<EventCollectBean>("EventCollectBean")
            .observe(this, {
                if (mArticleAdapter.itemCount <= 1) return@observe
                for (index in 1 until mArticleAdapter.itemCount) {
                    val bean = mArticleAdapter.getItemData(index)
                    if (bean?.id == it.articleId) {
                        bean.collect = it.isCollect
                        mArticleAdapter.notifyItemChanged(index + 1)
                    }
                }
            })
    }

    private fun initSwipeRefresh() {
        mBinding.swipeRefresh.setColorSchemeColors(R.color.color_0099ff.getResColor())
    }

    private fun initRecycler() {
        mBinding.recyclerArticle.layoutManager = LinearLayoutManager(context)
        mBinding.recyclerArticle.adapter = mArticleAdapter
        mArticleAdapter.withLoadStateFooter(FooterAdapter(mArticleAdapter))
    }


    private fun initTopSearch() {
        mBinding.topSear.viewBg.setOnClickListener {
            goSearch()
        }
        mBinding.topSear.ivAdd.setOnClickListener {
            goAdd()
        }
        mBinding.topSear.recyclerHotKey.layoutManager = LinearLayoutManager(context)
        mBinding.topSear.recyclerHotKey.adapter = mHotKeyAdapter
        //这段代码得放到 layoutManager 设置了之后，要不然没有效果
        bannerHelper =
            SimpleBannerHelper(mBinding.topSear.recyclerHotKey, RecyclerView.VERTICAL)
        mHotKeyAdapter.setOnItemClickListener { adapter, view, position ->
            goSearch()
        }
    }

    override fun startObserver() {
        super.startObserver()
        viewModel.mHotKeyList.observe(this, {
            mHotKeyAdapter.setNewInstance(it)
            bannerHelper.startTimerTask()
        })
        viewModel.mBannerList.observe(this, {
            mArticleAdapter.setBannerList(it)
        })
        mBinding.swipeRefresh.setOnRefreshListener {
            mArticleAdapter.refresh()
        }

        //因为刷新前也会调用LoadState.NotLoading，所以用一个外部变量判断是否是刷新后
        var hasRefreshing = false
        mArticleAdapter.addLoadStateListener {
            //判断是刷新状态
            when (it.refresh) {
//                加载中 (加载数据时候回调)
                is LoadState.Loading -> {
                    hasRefreshing = true
                    //如果是手动下拉刷新，则不展示loading页
                    if (!mBinding.swipeRefresh.isRefreshing) {
                        showLoading()
                    }
                }
//             没有加载中 (加载数据前和加载数据完成后回调)
                is LoadState.NotLoading -> {
                    if (hasRefreshing) {
                        hasRefreshing = false
                        if (mBinding.swipeRefresh.isRefreshing) {
                            mBinding.swipeRefresh.isRefreshing = false
                        } else {
                            dismissLoading()
                        }
                        //如果第一页数据就没有更多了，第一页不会触发append
                        if (it.source.append.endOfPaginationReached) {
                            //第一页就没有数据了，处理  retry
                        }
                    }
                }
//                加载失败 （加载数据失败回调）
                is LoadState.Error -> {

                }
            }
        }

        //因为刷新前也会调用LoadState.NotLoading，所以用一个外部变量判断是否是加载更多后
        var hasLoadingMore = false
        mArticleAdapter.addLoadStateListener {
            when (it.append) {
                is LoadState.Loading -> {
                    hasLoadingMore = true
                }
                is LoadState.NotLoading -> {
                    if (hasLoadingMore) {
                        hasLoadingMore = false
                        if (it.source.append.endOfPaginationReached) {
                            //没有更多了(只能用source的append)
                        } else {
                        }
                    }
                }
                is LoadState.Error -> {
                }
            }
        }


        mBinding.recyclerArticle.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
            }

            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                val layoutManager = recyclerView.layoutManager
                //判断是当前layoutManager是否为LinearLayoutManager
                // 只有LinearLayoutManager才有查找第一个和最后一个可见view位置的方法
                if (layoutManager is LinearLayoutManager) {
                    val linearManager = layoutManager
                    //获取最后一个可见view的位置
                    val lastItemPosition = linearManager.findLastVisibleItemPosition()
                    if (lastItemPosition >= 20) {
                        viewModel.mFabVisible.set(true)
                    } else {
                        viewModel.mFabVisible.set(false)
                    }
                }
            }
        })

        viewModel.mFabClick.observe(this, {
            if (it) {
                mBinding.recyclerArticle.smoothScrollToPosition(0)
            }
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
        //当前展示的标题
        val title = mHotKeyAdapter.getItem(bannerHelper.findLastVisibleItemPosition()).name
        val intent = Intent(requireContext(), SearchActivity::class.java)
        intent.putExtra("search_key", title)
        startActivity(intent)
    }

    override fun initView(view: View) {
    }
}