package com.bbq.system.ui

import android.view.View
import com.bbq.base.base.BaseVMFragment
import com.bbq.system.R
import com.bbq.system.databinding.FragmentSystemBinding


class SystemFragment : BaseVMFragment<FragmentSystemBinding>() {
    private val TAG = "SystemFragment"
    override fun getLayoutId(): Int {
        return R.layout.fragment_system
    }


    override fun initView(view: View) {


    }


}