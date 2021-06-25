package com.bbq.wanandroid.app

import android.content.Context
import androidx.multidex.MultiDex
import com.alibaba.android.arouter.launcher.ARouter
import com.bbq.base.base.BaseApp
import com.bbq.base.loadsir.EmptyCallback
import com.bbq.base.loadsir.ErrorCallback
import com.bbq.base.loadsir.LoadingCallback
import com.bbq.base.view.CustomBlackToastStyle
import com.hjq.toast.ToastUtils
import com.kingja.loadsir.core.LoadSir
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
        ARouter.init(this)
        LoadSir.beginBuilder()
            .addCallback(ErrorCallback())
            .addCallback(LoadingCallback())
            .addCallback(EmptyCallback())
            .setDefaultCallback(LoadingCallback::class.java)
            .commit()
    }

}