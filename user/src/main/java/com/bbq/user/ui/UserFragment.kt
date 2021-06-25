package com.bbq.user.ui

import com.bbq.user.R
import com.bbq.user.databinding.FragmentUserBinding
import com.bbq.base.base.BaseVMFragment

class UserFragment : BaseVMFragment<FragmentUserBinding>() {
    override fun getLayoutId(): Int {
        return R.layout.fragment_user
    }
}