package com.bbq.navigation.ui

import android.view.View
import com.bbq.navigation.R
import com.bbq.navigation.databinding.FragmentNavigationBinding
import com.bbq.base.base.BaseVMFragment

class NavigationFragment : BaseVMFragment<FragmentNavigationBinding>() {
    override fun getLayoutId(): Int {
        return R.layout.fragment_navigation
    }

    override fun initView(view: View) {
    }
}