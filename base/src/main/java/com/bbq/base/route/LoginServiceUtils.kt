package com.bbq.base.route

import android.content.Context
import androidx.lifecycle.LiveData
import com.alibaba.android.arouter.launcher.ARouter
import com.bbq.base.bean.User

class LoginServiceUtils {
    companion object {
        fun isLogin(): Boolean {
            return ARouter.getInstance().navigation(LoginService::class.java).isLogin()
        }

        fun getUserInfo(): User? {
            return ARouter.getInstance().navigation(LoginService::class.java).getUserInfo()
        }

        fun removeUserInfo() {
            ARouter.getInstance().navigation(LoginService::class.java).removeUserInfo()
        }

        fun start(context: Context) {
            ARouter.getInstance().navigation(LoginService::class.java).start(context)

        }

        fun getLiveData(): LiveData<User> {
            return ARouter.getInstance().navigation(LoginService::class.java).getLiveData()
        }
    }


}