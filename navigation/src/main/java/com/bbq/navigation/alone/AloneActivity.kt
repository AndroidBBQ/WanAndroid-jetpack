package com.bbq.navigation.alone

import android.os.Bundle
import com.bbq.base.base.BaseVMActivity
import com.bbq.navigation.R
import com.bbq.navigation.databinding.ActivityAloneBinding
import com.bbq.navigation.ui.NavigationFragment

class AloneActivity : BaseVMActivity<ActivityAloneBinding>() {
    override fun initView(savedInstanceState: Bundle?) {
        supportFragmentManager.beginTransaction().apply {
            add(R.id.fragment_layout, NavigationFragment())
            commit()
        }
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_alone
    }

}