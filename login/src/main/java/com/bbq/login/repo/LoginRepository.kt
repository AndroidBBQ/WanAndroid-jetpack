package com.bbq.login.repo

import com.bbq.base.rository.BaseRepository
import com.bbq.base.rository.StateLiveData
import com.bbq.login.api.LoginApi
import com.bbq.base.bean.User

class LoginRepository(private val service: LoginApi) : BaseRepository() {

    suspend fun login(username: String, password: String, result: StateLiveData<User>) {
        executeRequest({ service.login(username, password) }, result)
    }

    suspend fun register(
        username: String,
        password: String,
        surePassword: String,
        result: StateLiveData<User>
    ) {
        executeRequest({ service.register(username, password, surePassword) }, result)
    }

}