package com.bbq.base.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding

abstract class BaseVMFragment<T : ViewDataBinding> : BaseFragment() {

    lateinit var mBinding: T

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mBinding = DataBindingUtil.inflate(inflater, getLayoutId(), container, false)
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
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