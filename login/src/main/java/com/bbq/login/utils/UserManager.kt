package com.bbq.login.utils

import androidx.lifecycle.MutableLiveData
import com.bbq.base.utils.SpUtils
import com.bbq.base.bean.User

object UserManager {

    private const val USER_DATA: String = "user_data"

    private val liveData = MutableLiveData<User>()

    fun getLoginLiveData(): MutableLiveData<User> {
        return liveData
    }

    fun getUser(): User? {
        return SpUtils.getParcelable(USER_DATA)
    }

    fun saveUser(user: User) {
        SpUtils.put(USER_DATA, user)
        if (liveData.hasObservers()) {
            liveData.postValue(user)
        }
    }

    fun isLogin(): Boolean {
        return getUser() != null
    }

    fun removeUser() {
        SpUtils.removeKey(USER_DATA)
    }

}