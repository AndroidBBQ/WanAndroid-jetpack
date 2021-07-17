package com.bbq.user.di

import com.bbq.net.net.RetrofitManager
import com.bbq.user.api.UserApi
import com.bbq.user.repo.UserRepo
import com.bbq.user.viewmodel.UserVM
import org.koin.android.ext.koin.androidApplication
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module


val userModel = module {
    single {
        RetrofitManager.instance.create(UserApi::class.java)
    }
    single {
        UserRepo(get())
    }
    viewModel {
        UserVM(androidApplication(), get())
    }
}