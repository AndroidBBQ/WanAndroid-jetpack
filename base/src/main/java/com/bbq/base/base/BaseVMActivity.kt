package com.bbq.base.base

import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding

/**
 * 这里只传入 databinding 是由于 viewmodel要使用的话 可以直接通过koin注解
 */
abstract class BaseVMActivity<T : ViewDataBinding> : BaseActivity() {


    lateinit var mBinding: T

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = DataBindingUtil.setContentView(this, getLayoutId())
        mBinding.lifecycleOwner = this
        initData()
        startObserver()
    }

    open fun startObserver() {

    }

    open fun initData() {

    }


    override fun onDestroy() {
        super.onDestroy()
        mBinding.unbind()
    }


    abstract fun getLayoutId(): Int


}