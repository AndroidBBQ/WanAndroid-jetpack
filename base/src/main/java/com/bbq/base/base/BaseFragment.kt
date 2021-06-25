package com.bbq.base.base

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.bbq.base.view.LoadingDialog
import com.hjq.toast.ToastUtils

abstract class BaseFragment : Fragment() {

    private lateinit var mLoadingDialog: LoadingDialog


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mLoadingDialog = LoadingDialog(view.context)
    }


    /**
     * show 加载中
     */
    fun showLoading() {
        mLoadingDialog.showDialog(requireContext())
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