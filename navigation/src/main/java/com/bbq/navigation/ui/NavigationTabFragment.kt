package com.bbq.navigation.ui

import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.bbq.base.base.BaseVMFragment
import com.bbq.base.loadsir.EmptyCallback
import com.bbq.base.loadsir.ErrorCallback
import com.bbq.base.route.WebServiceUtils
import com.bbq.navigation.R
import com.bbq.navigation.adapter.NavTabLeftAdapter
import com.bbq.navigation.adapter.NavTabRightAdapter
import com.bbq.navigation.bean.ArticleBean
import com.bbq.navigation.bean.NavTabBean
import com.bbq.navigation.databinding.NavFragmentNavigationTabBinding
import com.bbq.navigation.viewmodel.NavTabVM
import com.bbq.net.model.DataStatus
import com.kingja.loadsir.core.LoadService
import com.kingja.loadsir.core.LoadSir
import org.koin.androidx.viewmodel.ext.android.viewModel

class NavigationTabFragment : BaseVMFragment<NavFragmentNavigationTabBinding>() {

    private val viewModel: NavTabVM by viewModel()

    private val mLeftList by lazy {
        mutableListOf<NavTabBean>()
    }

    private val mRightList by lazy {
        mutableListOf<ArticleBean>()
    }

    private val mLeftAdapter by lazy {
        NavTabLeftAdapter(mLeftList)
    }

    private val mRightAdapter by lazy {
        NavTabRightAdapter(mRightList)
    }

    private val mLoadSir by lazy {
        val loadSir = LoadSir.Builder()
            .addCallback(EmptyCallback())
            .addCallback(ErrorCallback())
            .setDefaultCallback(ErrorCallback::class.java)
            .build()

        loadSir.register(mBinding.root) {
            viewModel.getNavList()
        }
    }

    private lateinit var mCurrentNavTab: NavTabBean

    override fun initView(view: View) {
        mBinding.recyclerLeft.layoutManager = LinearLayoutManager(requireContext())
        mBinding.recyclerLeft.adapter = mLeftAdapter

        mBinding.recyclerRight.layoutManager = LinearLayoutManager(requireContext())
        mBinding.recyclerRight.adapter = mRightAdapter

        mLeftAdapter.setOnItemClickListener { adapter, view, position ->
            if (mLeftList[position].cid == mCurrentNavTab.cid) return@setOnItemClickListener
            //选中当前的
            mLeftList.forEachIndexed { index, navTabBean ->
                if (index == position) {
                    navTabBean.isSelected = true
                    mCurrentNavTab = navTabBean
                    switchRightData()
                } else {
                    navTabBean.isSelected = false
                }
                mLeftAdapter.notifyDataSetChanged()
            }
        }
        mRightAdapter.setOnItemClickListener { adapter, view, position ->
            val bean = mRightList[position]
            WebServiceUtils.goWeb(requireContext(), bean.title, bean.link)
        }

    }

    override fun initData() {
        super.initData()
        viewModel.getNavList()
    }

    override fun startObserver() {
        super.startObserver()
        viewModel.mLeftList.observe(this, {
            when (it.dataStatus) {
                DataStatus.STATE_LOADING -> {
                    showLoading()
                }
                DataStatus.STATE_ERROR -> {
                    dismissLoading()
//                    it?.exception?.msg?.showToast()
                    mLoadSir.showCallback(ErrorCallback::class.java)
                }
                DataStatus.STATE_SUCCESS -> {
                    dismissLoading()
                    if (it.data.isNullOrEmpty()) {
//                        toast("数据为空!")
                        mLoadSir.showCallback(EmptyCallback::class.java)
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
    }

    private fun switchRightData() {
        mRightList.clear()
        if (!mCurrentNavTab.articles.isNullOrEmpty()) {
            mRightList.addAll(mCurrentNavTab.articles!!)
        }
        mRightAdapter.notifyDataSetChanged()
    }

    override fun getLayoutId(): Int {
        return R.layout.nav_fragment_navigation_tab
    }
}