package com.bbq.login.di

import com.bbq.login.api.LoginApi
import com.bbq.login.repo.LoginRepository
import com.bbq.net.net.RetrofitManager
import com.bbq.login.viewmodel.LoginViewModel
import org.koin.android.ext.koin.androidApplication
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module


val loginModel = module {
    single {
        RetrofitManager.instance.create(LoginApi::class.java)
    }
    single {
        LoginRepository(get())
    }

    viewModel {
        LoginViewModel(androidApplication(), get())
    }
}