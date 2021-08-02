package com.bbq.wanandroid.app

import android.content.Context
import androidx.loader.content.Loader
import androidx.multidex.MultiDex
import com.alibaba.android.arouter.launcher.ARouter
import com.bbq.base.base.BaseApp
import com.bbq.base.loadsir.EmptyCallback
import com.bbq.base.view.CustomBlackToastStyle
import com.bbq.wanandroid.BuildConfig
import com.hjq.permissions.XXPermissions
import com.hjq.toast.ToastUtils
import com.kingja.loadsir.core.LoadSir
import com.scwang.smart.refresh.layout.SmartRefreshLayout
import com.tencent.mmkv.MMKV
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class WanApp : BaseApp() {
    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
        MultiDex.install(this)
    }

    override fun onCreate() {
        super.onCreate()
        MMKV.initialize(this)
        startKoin {
            androidContext(this@WanApp)
            modules(appModule)
        }
        val toastStyle = CustomBlackToastStyle()
        ToastUtils.init(this, toastStyle)
        if (BuildConfig.DEBUG) {
            ARouter.openLog()
            ARouter.openDebug()
        }
        ARouter.init(this)
        // 当前项目是否已经适配了分区存储的特性
        XXPermissions.setScopedStorage(true)
        //让SmartRefreshLayout可以拉伸的更有弹性
        SmartRefreshLayout.setDefaultRefreshInitializer { context, layout ->
            layout.setEnableOverScrollDrag(true)
            layout.setFooterMaxDragRate(4.0f)
        }
        //loadsir
        LoadSir.beginBuilder()
            .addCallback(EmptyCallback())
            .addCallback(EmptyCallback())
            .commit()
    }

}