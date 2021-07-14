package com.bbq.webview.di

import com.bbq.net.net.RetrofitManager
import com.bbq.webview.api.WebApi
import com.bbq.webview.repo.WebRepo
import com.bbq.webview.viewmodel.WebViewModel
import org.koin.android.ext.koin.androidApplication
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module


val webModels = module {
    single {
        RetrofitManager.instance.create(WebApi::class.java)
    }
    single {
        WebRepo(get())
    }
    viewModel { WebViewModel(androidApplication(), get()) }
}