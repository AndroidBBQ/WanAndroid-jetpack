package com.bbq.project.ui

import android.view.View
import com.bbq.project.R
import com.bbq.project.databinding.FragmentProjectBinding
import com.bbq.base.base.BaseVMFragment

class ProjectFragment : BaseVMFragment<FragmentProjectBinding>() {
    override fun getLayoutId(): Int {
        return R.layout.fragment_project
    }

    override fun initView(view: View) {
    }
}