package com.bbq.login.route

import android.content.Context
import android.content.Intent
import androidx.lifecycle.LiveData
import com.alibaba.android.arouter.facade.annotation.Route
import com.bbq.base.bean.User
import com.bbq.base.route.LoginService
import com.bbq.login.ui.LoginActivity
import com.bbq.login.utils.UserManager

@Route(path = "/login/login")
class LoginServiceImpl : LoginService {

    override fun isLogin(): Boolean {
        return UserManager.isLogin()
    }

    override fun getUserInfo(): User? {
        return UserManager.getUser()
    }

    override fun removeUserInfo() {
        UserManager.removeUser()
    }

    override fun start(context: Context) {
        val intent = Intent(context, LoginActivity::class.java)
        context.startActivity(intent)
    }

    override fun getLiveData(): LiveData<User> {
        return UserManager.getLoginLiveData()
    }

    override fun init(context: Context?) {

    }
}