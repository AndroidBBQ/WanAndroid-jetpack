package com.bbq.wanandroid

import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.bbq.base.base.BaseVMActivity
import com.bbq.wanandroid.databinding.ActivityMainBinding

class MainActivity : BaseVMActivity<ActivityMainBinding>() {


    override fun getLayoutId(): Int {
        return R.layout.activity_main
    }

    override fun initData() {
        super.initData()
        var navHostFragment: NavHostFragment =
            supportFragmentManager.findFragmentById(R.id.fragment_nav) as NavHostFragment
        var navController: NavController = navHostFragment.navController
        NavigationUI.setupWithNavController(mBinding.bottomNav, navController)
        mBinding.bottomNav.itemTextAppearanceActive = R.style.bottom_selected_text;
        mBinding.bottomNav.itemTextAppearanceInactive = R.style.bottom_normal_text;
    }

    //需要重写onSupportNavigateUp方法
    override fun onSupportNavigateUp(): Boolean {
        return Navigation.findNavController(this, R.id.fragment_nav).navigateUp()
    }

}