package com.bbq.base.base

import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import com.bbq.base.R
import com.bbq.base.utils.getResString

/**
 * 这里只传入 databinding 是由于 viewmodel要使用的话 可以直接通过koin注解
 */
abstract class BaseVMActivity<T : ViewDataBinding> : BaseActivity() {


    lateinit var mBinding: T

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = DataBindingUtil.setContentView(this, getLayoutId())
        mBinding.lifecycleOwner = this
        initView(savedInstanceState)
        initData()
        startObserver()
    }

    open fun startObserver() {

    }

    open fun initData() {}

    abstract fun initView(savedInstanceState: Bundle?);


    override fun onDestroy() {
        super.onDestroy()
        mBinding.unbind()
    }


    abstract fun getLayoutId(): Int


    fun getClickEmptyDataView(parent: RecyclerView): View {
        val notDataView: View = layoutInflater.inflate(R.layout.empty_view, parent, false)
        notDataView.setOnClickListener { onClickRetry() }
        return notDataView
    }

    fun getClickErrorView(parent: RecyclerView): View {
        val errorView: View = layoutInflater.inflate(R.layout.error_view, parent, false)
        errorView.setOnClickListener { onClickRetry() }
        return errorView
    }

    fun getMsgEmptyDataView(
        parent: RecyclerView,
        msg: String? = R.string.base_no_data.getResString()
    ): View {
        val notDataView: View = layoutInflater.inflate(R.layout.empty_view, parent, false)
        notDataView.findViewById<TextView>(R.id.tv_nodata).text = msg
        return notDataView
    }

    fun getMsgErrorView(
        parent: RecyclerView,
        msg: String? = R.string.base_net_error.getResString()
    ): View {
        val errorView: View = layoutInflater.inflate(R.layout.error_view, parent, false)
        errorView.findViewById<TextView>(R.id.tv_error_msg).text = msg
        return errorView
    }

    open fun onClickRetry() {

    }


}