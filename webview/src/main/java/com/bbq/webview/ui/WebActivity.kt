package com.bbq.webview.ui

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.KeyEvent
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import android.widget.LinearLayout
import androidx.lifecycle.lifecycleScope
import com.bbq.base.base.BaseVMActivity
import com.bbq.base.bean.EventCollectBean
import com.bbq.base.route.LoginServiceUtils
import com.bbq.base.utils.LiveDataBus
import com.bbq.base.utils.StatusBarUtil
import com.bbq.base.utils.getResColor
import com.bbq.webview.R
import com.bbq.webview.databinding.ActivityWebBinding
import com.bbq.webview.viewmodel.WebViewModel
import com.just.agentweb.AgentWeb
import org.koin.androidx.viewmodel.ext.android.viewModel


class WebActivity : BaseVMActivity<ActivityWebBinding>() {
    private lateinit var mTitle: String
    private lateinit var mUrl: String
    private var mId: Int = -1
    private var mCollect = false
    val viewModel: WebViewModel by viewModel()
    private lateinit var mAgentWeb: AgentWeb

    override fun initView(savedInstanceState: Bundle?) {
        StatusBarUtil.setColor(this, R.color.theme.getResColor())
        mBinding.vm = viewModel
        mTitle = intent.getStringExtra(key_title) ?: ""
        mUrl = intent.getStringExtra(key_url) ?: ""
        mId = intent.getIntExtra(key_id, -1)
        mCollect = intent.getBooleanExtra(key_collect, false)
        viewModel.setTitle(mTitle)
        if (mId == -1) {
            viewModel.mTitleVM.mRightDrawable.set(null)
        } else {
            viewModel.setCollectState(mCollect)
            viewModel.mTitleVM.rightAction = {
                if (!LoginServiceUtils.isLogin()) {
                    //如果没有登录，先登录
                    LoginServiceUtils.start(this)
                } else {
                    if (mCollect) {
                        lifecycleScope.launchWhenCreated {
                            val result = viewModel.unCollect(mId)
                            if (result) {
                                viewModel.setCollectState(false)
                                val message = EventCollectBean(mId, false)
                                LiveDataBus.with<EventCollectBean>("EventCollectBean")
                                    .postValue(message)
                                toast("取消收藏成功！")
                            } else {
                                toast("取消收藏失败!")
                            }
                        }
                    } else {
                        //收藏
                        lifecycleScope.launchWhenCreated {
                            val result = viewModel.collect(mId)
                            if (result) {
                                viewModel.setCollectState(true)
                                val message = EventCollectBean(mId, true)
                                LiveDataBus.with<EventCollectBean>("EventCollectBean")
                                    .postValue(message)
                                toast("收藏成功！")
                            } else {
                                toast("收藏失败!")
                            }
                        }
                    }
                }
            }
        }
        webViewSetting()
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun webViewSetting() {
        mAgentWeb = AgentWeb.with(this)
            .setAgentWebParent(
                (mBinding.webContainer as LinearLayout?)!!,
                ViewGroup.LayoutParams(-1, -1)
            )
            .useDefaultIndicator()
            .createAgentWeb()
            .ready()
            .go(mUrl)

        mAgentWeb.webCreator.webView.setOnTouchListener { v, event ->
            (v as WebView).requestDisallowInterceptTouchEvent(true)
            false
        }
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_web
    }

    override fun startObserver() {
        super.startObserver()
        viewModel.mIsFinish.observe(this, {
            if (it) finish()
        })

        viewModel.mScrollToTop.observe(this, {
//            if (it) mBinding.nsv.smoothScrollTo(0, 0, 500)
        })
    }

    companion object {
        val key_title = "key_title"
        val key_url = "key_url"
        val key_id = "key_id"
        val key_collect = "key_collect"
        fun launch(context: Context, title: String, url: String, id: Int?, isCollect: Boolean) {
            val intent = Intent(context, WebActivity::class.java)
            intent.putExtra(key_title, title)
            intent.putExtra(key_url, url)
            intent.putExtra(key_id, id)
            intent.putExtra(key_collect, isCollect)
            context.startActivity(intent)
        }
    }


    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if (mAgentWeb.handleKeyEvent(keyCode, event)) {
            return true
        }
        return super.onKeyDown(keyCode, event)
    }


    override fun onPause() {
        mAgentWeb.webLifeCycle.onPause()
        super.onPause()
    }

    override fun onResume() {
        mAgentWeb.webLifeCycle.onResume()
        super.onResume()
    }

    override fun onDestroy() {
        mAgentWeb.webLifeCycle.onDestroy()
        super.onDestroy()
    }
}