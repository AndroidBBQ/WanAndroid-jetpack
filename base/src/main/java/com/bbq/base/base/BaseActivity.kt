package com.bbq.base.base

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bbq.base.R
import com.bbq.base.utils.ActivityUtil
import com.bbq.base.utils.StatusBarUtil
import com.bbq.base.utils.getResColor
import com.bbq.base.view.LoadingDialog
import com.hjq.toast.ToastUtils

open class BaseActivity : AppCompatActivity() {
    private lateinit var mLoadingDialog: LoadingDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mLoadingDialog = LoadingDialog(this)
        ActivityUtil.addActivity(this)
        //向上便宜
        StatusBarUtil.setTransparentForWindow(this)
        //让模式我深色模式，也就是状态懒字体变黑
//        StatusBarUtil.setDarkMode(this)
    }

    override fun onDestroy() {
        super.onDestroy()
        ActivityUtil.removeActivity(this)

    }

    /**
     * show 加载中
     */
    fun showLoading() {
        mLoadingDialog.showDialog(this)
    }

    /**
     * dismiss loading dialog
     */
    fun dismissLoading() {
        mLoadingDialog.dismissDialog()
    }

    fun toast(message: String) {
        ToastUtils.show(message)
    }
}