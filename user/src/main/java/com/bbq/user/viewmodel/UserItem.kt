package com.bbq.user.viewmodel

import androidx.databinding.BaseObservable
import com.bbq.base.route.LoginServiceUtils
import com.bbq.user.BR

class UserItem : BaseObservable() {
    var mUserName = "去登录"
        get() {
            return if (LoginServiceUtils.isLogin()) {
                LoginServiceUtils.getUserInfo()?.username!!
            } else {
                "去登录"
            }
        }

    var mBtnIsShow = false
        get() {
            return LoginServiceUtils.isLogin()
        }

    fun refresh() {
        notifyPropertyChanged(BR._all)
    }

}