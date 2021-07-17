package com.bbq.base.route

import android.content.Context
import androidx.lifecycle.LiveData
import com.alibaba.android.arouter.launcher.ARouter
import com.bbq.base.bean.User

class WebServiceUtils {
    companion object {
        fun goWeb(
            context: Context,
            title: String,
            url: String,
            id: Int = -1,
            isCollect: Boolean = false
        ) {
            ARouter.getInstance().navigation(WebService::class.java)
                .goWeb(context, title, url, id, isCollect)
        }
    }
}

class CollectionServiceUtils {
    companion object {
        fun launch(context: Context) {
            ARouter.getInstance().navigation(CollectionService::class.java)
                .launch(context)
        }
    }

}


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