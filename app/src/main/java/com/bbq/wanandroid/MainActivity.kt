package com.bbq.wanandroid

import android.os.Bundle
import androidx.navigation.NavController
import androidx.navigation.NavGraph
import androidx.navigation.NavGraphNavigator
import androidx.navigation.NavigatorProvider
import androidx.navigation.fragment.NavHostFragment
import com.bbq.base.base.BaseVMActivity
import com.bbq.base.utils.FixFragmentNavigator
import com.bbq.home.ui.FaqFragment
import com.bbq.home.ui.HomeFragment
import com.bbq.navigation.ui.NavigationFragment
import com.bbq.user.ui.UserFragment
import com.bbq.wanandroid.databinding.ActivityMainBinding


class MainActivity : BaseVMActivity<ActivityMainBinding>() {

    private var navController: NavController? = null
    override fun getLayoutId(): Int {
        return R.layout.activity_main
    }

    override fun initView(savedInstanceState: Bundle?) {
        if (savedInstanceState == null) {
            setupBottomNavigationBar()
        }

    }

    private fun setupBottomNavigationBar() {
        var navHostFragment: NavHostFragment =
            supportFragmentManager.findFragmentById(R.id.fragment_nav) as NavHostFragment
        navController = navHostFragment.navController
        //创建自定义的Fragment导航器
        //创建自定义的Fragment导航器
        val fragmentNavigator =
            FixFragmentNavigator(this, navHostFragment.childFragmentManager, navHostFragment.id)
        //获取导航器提供者
        val provider = navController!!.navigatorProvider
        //把自定义的Fragment导航器添加进去
        provider.addNavigator(fragmentNavigator)
        //手动创建导航图
        val navGraph = initNavGraph(provider, fragmentNavigator)
        //设置导航图
        navController!!.setGraph(navGraph)

//        NavigationUI.setupWithNavController(mBinding.bottomNav, navController)
        //底部导航设置点击事件
        //底部导航设置点击事件
        mBinding.bottomNav.setOnNavigationItemSelectedListener { item ->
            navController!!.navigate(item.getItemId())
            true
        }

        mBinding.bottomNav.itemTextAppearanceActive = R.style.bottom_selected_text;
        mBinding.bottomNav.itemTextAppearanceInactive = R.style.bottom_normal_text;
    }

    //手动创建导航图，把3个目的地添加进来
    private fun initNavGraph(
        provider: NavigatorProvider,
        fragmentNavigator: FixFragmentNavigator
    ): NavGraph {
        val navGraph = NavGraph(NavGraphNavigator(provider))

        //用自定义的导航器来创建目的地
        val destination1 = fragmentNavigator.createDestination()
        destination1.id = R.id.navigation_home
        destination1.className = HomeFragment::class.java.canonicalName
        destination1.label = resources.getString(R.string.HomeFragment)
        navGraph.addDestination(destination1)

        val destination2 = fragmentNavigator.createDestination()
        destination2.id = R.id.navigation_faq
        destination2.className = FaqFragment::class.java.canonicalName
        destination2.label = resources.getString(R.string.FaqFragment)
        navGraph.addDestination(destination2)

        val destination3 = fragmentNavigator.createDestination()
        destination3.id = R.id.navigation_navi
        destination3.className = NavigationFragment::class.java.canonicalName
        destination3.label = resources.getString(R.string.NavigationFragment)
        navGraph.addDestination(destination3)


        val destination5 = fragmentNavigator.createDestination()
        destination5.id = R.id.navigation_mine
        destination5.className = UserFragment::class.java.canonicalName
        destination5.label = resources.getString(R.string.UserFragment)
        navGraph.addDestination(destination5)

        navGraph.startDestination = R.id.navigation_home
        return navGraph
    }


    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        setupBottomNavigationBar()
    }

    override fun onBackPressed() {
        val currentId: Int = navController?.currentDestination!!.getId()
        val startId: Int = navController?.graph!!.getStartDestination()
        //如果当前目的地不是HomeFragment，则先回到HomeFragment
        if (currentId != startId) {
            mBinding.bottomNav.selectedItemId = startId
        } else {
            finish()
        }
    }


}