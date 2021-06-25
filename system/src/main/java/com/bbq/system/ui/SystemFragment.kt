package com.bbq.system.ui

import com.bbq.system.R
import com.bbq.system.databinding.FragmentSystemBinding
import com.bbq.base.base.BaseVMFragment

class SystemFragment : BaseVMFragment<FragmentSystemBinding>() {
    override fun getLayoutId(): Int {
        return R.layout.fragment_system
    }
}