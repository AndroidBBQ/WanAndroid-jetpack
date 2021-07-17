package com.bbq.home.ui

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.alibaba.android.arouter.launcher.ARouter
import com.bbq.base.base.BaseVMActivity
import com.bbq.base.bean.EventCollectBean
import com.bbq.base.route.LoginServiceUtils
import com.bbq.base.route.WebService
import com.bbq.base.utils.KeyBoardUtils
import com.bbq.base.utils.LiveDataBus
import com.bbq.base.utils.SpUtils
import com.bbq.home.R
import com.bbq.home.adapter.HistoryAdapter
import com.bbq.home.adapter.HomeArticleAdapter
import com.bbq.home.databinding.ActivitySearchBinding
import com.bbq.home.viewmodel.SearchActivityVM
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import org.koin.androidx.viewmodel.ext.android.viewModel


class SearchActivity : BaseVMActivity<ActivitySearchBinding>() {
    private val viewModel: SearchActivityVM by viewModel()
    private var mPage = 0
    private val mHistoryAdapter by lazy {
        HistoryAdapter(viewModel.mHistoryList.value)
    }

    private val mArticleAdapter by lazy {
        HomeArticleAdapter(viewModel.mArticleList.value)
    }

    override fun initView(savedInstanceState: Bundle?) {
        mBinding.topSear.search.setText(intent.getStringExtra("search_key") ?: "")
        mBinding.vm = viewModel
        initfab()
        initTopSearch()
        initHistory()
        initSearch()
        LiveDataBus.with<EventCollectBean>("EventCollectBean")
            .observe(this, {
                mArticleAdapter.data.forEachIndexed { index, articleBean ->
                    if (articleBean.id == it.articleId) {
                        articleBean.collect = it.isCollect
                        mArticleAdapter.notifyItemChanged(index)
                    }
                }
            })
    }

    private fun initSearch() {
        mBinding.recyclerSearchResult.layoutManager = LinearLayoutManager(baseContext)
        mBinding.recyclerSearchResult.adapter = mArticleAdapter
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

        mBinding.smartRefresh.setOnRefreshListener {
            mPage = 0
            // 这里的作用是防止下拉刷新的时候还可以上拉加载,请求成功后要设置为true
            mArticleAdapter.loadMoreModule.isEnableLoadMore = false
            viewModel.searchArticles(mPage, mBinding.topSear.search.text?.trim().toString())
        }
        mArticleAdapter.loadMoreModule.setOnLoadMoreListener {
            mPage++
            viewModel.searchArticles(mPage, mBinding.topSear.search.text?.trim().toString())
        }
        mArticleAdapter.loadMoreModule.isAutoLoadMore = true
        mArticleAdapter.loadMoreModule.isEnableLoadMoreIfNotFullPage = false


    }

    private fun initfab() {
        viewModel.getHotKeys()
    }

    private fun initHistory() {
        mBinding.recyHistory.layoutManager = LinearLayoutManager(baseContext)
        mBinding.recyHistory.adapter = mHistoryAdapter
        mHistoryAdapter.setOnItemClickListener { adapter, view, position ->
            mBinding.topSear.search.setText(mHistoryAdapter.getItem(position))
            search(mHistoryAdapter.getItem(position))
        }
        mHistoryAdapter.setOnItemChildClickListener { adapter, view, position ->
            if (view.id == R.id.delete) {
                mHistoryAdapter.removeAt(position)
                if (mHistoryAdapter.data.size <= 0) {
                    mBinding.recyHistory.visibility = View.GONE
                    mBinding.tvHistoryTitle.visibility = View.GONE
                }
                //保存history的值，可以使用mmkv进行保存
                SpUtils.put("search_history", Gson().toJson(mHistoryAdapter.data))
            }
        }
        viewModel.getHistory()
    }

    override fun startObserver() {
        super.startObserver()
        viewModel.mHistoryList.observe(this, {
            if (it.isNullOrEmpty()) {
                mBinding.tvHistoryTitle.visibility = View.GONE
                mBinding.recyHistory.visibility = View.GONE
            } else {
                mBinding.tvHistoryTitle.visibility = View.VISIBLE
                mBinding.recyHistory.visibility = View.VISIBLE
                mHistoryAdapter.setNewInstance(it)
            }
        })
        viewModel.mHotKeyList.observe(this, {
            if (it.isNullOrEmpty()) {
                mBinding.fbl.visibility = View.GONE
                mBinding.tvSearchTitle.visibility = View.GONE
                return@observe
            }
            mBinding.fbl.visibility = View.VISIBLE
            mBinding.tvSearchTitle.visibility = View.VISIBLE
            mBinding.fbl.removeAllViews()
            it.forEach { that ->
                val inflater = LayoutInflater.from(mBinding.fbl.context)
                val tv = inflater.inflate(R.layout.fbl_hot_key, mBinding.fbl, false) as TextView
                tv.text = that.name
                tv.setOnClickListener {
                    mBinding.topSear.search.setText(that.name)
                    search(that.name)
                }
                mBinding.fbl.addView(tv)
            }
        })
        viewModel.mArticleList.observe(this, {
            mArticleAdapter.loadMoreModule.isEnableLoadMore = true
            if (mPage == 0) {
                if (mBinding.smartRefresh.isRefreshing) {
                    mBinding.smartRefresh.finishRefresh()
                } else {
                    dismissLoading()
                }
            }
            if (it.isNullOrEmpty()) {
                mArticleAdapter.setNewInstance(mutableListOf())
                //说明是第一页，而且没有数据
                mArticleAdapter.setEmptyView(getMsgEmptyDataView(mBinding.recyclerSearchResult))
                return@observe
            }
            //有数据
            mArticleAdapter.setNewInstance(it)
        })

        viewModel.mMaxPage.observe(this, {
            if (mPage < it - 1) {
                mArticleAdapter.loadMoreModule.loadMoreComplete()
            } else {
                mArticleAdapter.loadMoreModule.loadMoreEnd()
            }
        })
        viewModel.mLoadError.observe(this, {
            if (mPage == 0) {
                if (mBinding.smartRefresh.isRefreshing) {
                    mBinding.smartRefresh.finishRefresh()
                } else {
                    dismissLoading()
                }
            }
            if (it) {
                //必须要先把数组设置为空
                mArticleAdapter.setNewInstance(mutableListOf())
                //如果网络错误了
                mArticleAdapter.setEmptyView(getMsgErrorView(mBinding.recyclerSearchResult))
//                mArticleAdapter.loadMoreModule.isEnableLoadMore=true
//                mArticleAdapter.loadMoreModule.loadMoreFail()
            }
        })
    }

    override fun onClickRetry() {
        super.onClickRetry()
        mPage = 0
        viewModel.searchArticles(mPage, mBinding.topSear.search.text?.trim().toString())
    }


    /**
     * 搜索
     */
    private fun search(item: String?) {
        if (item.isNullOrEmpty()) {
            toast("搜索条件不能为空！")
            return
        }
        //开始搜索
        initSearchVisible()
        KeyBoardUtils.hideKeyboard(mBinding.topSear.search)
        //将搜索条件保存
        saveKey(item)
        mPage = 0
        if (!mBinding.smartRefresh.isRefreshing) {
            showLoading()
        }
        viewModel.searchArticles(mPage, item)
    }

    private fun saveKey(item: String) {
        val history = SpUtils.getString("search_history")

        if (history.isNullOrEmpty()) {
            val tmp = mutableListOf<String>()
            tmp.add(item)
            SpUtils.put("search_history", Gson().toJson(tmp))
        } else {
            val hisList = Gson().fromJson<MutableList<String>>(
                history,
                object : TypeToken<MutableList<String>>() {}.type
            )
            if (!hisList.contains(item)) {
                hisList.add(item)
                SpUtils.put("search_history", Gson().toJson(hisList))
            }
        }
    }

    private fun initSearchVisible() {
        viewModel.mRecyclerVisible.set(true)
        mBinding.tvHistoryTitle.visibility = View.GONE
        mBinding.recyHistory.visibility = View.GONE
    }

    private fun initSearchNoVisible() {
        viewModel.mRecyclerVisible.set(false)
        viewModel.getHotKeys()
        viewModel.getHistory()
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun initTopSearch() {
        mBinding.topSear.tvCancel.setOnClickListener { finish() }
        mBinding.topSear.search.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                search(mBinding.topSear.search.text?.trim().toString())
                return@setOnEditorActionListener true
            }
            false
        }
        mBinding.topSear.search.setOnTouchListener { _, _ ->
            initSearchNoVisible()
            false
        }
        mBinding.topSear.search.addKeyboardListener(mBinding.root)
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_search
    }

}