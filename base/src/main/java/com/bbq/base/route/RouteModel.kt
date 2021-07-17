package com.bbq.base.route

import android.content.Context
import androidx.lifecycle.LiveData
import com.alibaba.android.arouter.facade.template.IProvider
import com.bbq.base.bean.User

interface WebService : IProvider {
    fun goWeb(context: Context, title: String, url: String, id: Int?, isCollect: Boolean)
}

interface LoginService : IProvider {

    fun isLogin(): Boolean

    fun getUserInfo(): User?

    fun removeUserInfo()

    fun start(context: Context)

    fun getLiveData(): LiveData<User>

}

interface CollectionService : IProvider {
    fun launch(context: Context)
}