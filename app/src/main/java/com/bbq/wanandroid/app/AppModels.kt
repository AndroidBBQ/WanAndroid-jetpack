package com.bbq.wanandroid.app

import com.bbq.home.di.homeModels
import com.bbq.login.di.loginModel
import com.bbq.net.net.RetrofitManager
import com.bbq.webview.di.webModels
import com.google.gson.GsonBuilder
import okhttp3.internal.immutableListOf
import org.koin.dsl.module

val otherModule = module {
    single {
        RetrofitManager.instance
    }

    single {
        GsonBuilder().disableHtmlEscaping().create()
    }
}


val appModule = immutableListOf(otherModule, homeModels, webModels, loginModel)