package com.bbq.faq.ui

import com.bbq.faq.R
import com.bbq.faq.databinding.FragmentFaqBinding
import com.bbq.base.base.BaseVMFragment

class FaqFragment : BaseVMFragment<FragmentFaqBinding>() {
    override fun getLayoutId(): Int {
        return R.layout.fragment_faq
    }
}