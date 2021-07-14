package com.bbq.login.viewmodel

import android.app.Application
import androidx.lifecycle.viewModelScope
import com.bbq.base.base.BaseViewModel
import com.bbq.base.rository.StateLiveData
import com.bbq.base.bean.User
import com.bbq.login.repo.LoginRepository
import kotlinx.coroutines.launch

/**
 * Create by liwen on 2020/5/27
 */
class LoginViewModel(application: Application, val loginRepo: LoginRepository) :
    BaseViewModel(application) {

    val mLoginUser = StateLiveData<User>()
    fun login(username: String, password: String) {
        viewModelScope.launch {
            loginRepo.login(username, password, mLoginUser)
        }
    }

    val mRegisterUser = StateLiveData<User>()
    fun register(username: String, password: String, surePassword: String) {
        viewModelScope.launch {
            loginRepo.register(username, password, surePassword, mRegisterUser)
        }
    }

}