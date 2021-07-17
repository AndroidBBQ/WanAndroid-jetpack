package com.bbq.navigation.di

import com.bbq.navigation.api.NavApi
import com.bbq.navigation.repo.NavRepo
import com.bbq.navigation.viewmodel.NavTabVM
import com.bbq.navigation.viewmodel.SystemListVM
import com.bbq.net.net.RetrofitManager
import org.koin.android.ext.koin.androidApplication
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module


val navModel = module {
    single {
        RetrofitManager.instance.create(NavApi::class.java)
    }

    single {
        NavRepo(get())
    }

    viewModel {
        NavTabVM(androidApplication(), get())
    }

    viewModel {
        SystemListVM(androidApplication(), get())
    }
}