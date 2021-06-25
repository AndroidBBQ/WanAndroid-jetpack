package com.bbq.home.di

import com.bbq.home.api.HomeApi
import com.bbq.home.repo.HomeRepo
import com.bbq.home.viewmodel.HomeViewModel
import com.bbq.net.net.RetrofitManager
import org.koin.android.ext.koin.androidApplication
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val homeModels = module {
    single {
        RetrofitManager.instance.create(HomeApi::class.java)
    }
    single {
        HomeRepo(get())
    }

    viewModel {
        HomeViewModel(androidApplication(), get())
    }
}