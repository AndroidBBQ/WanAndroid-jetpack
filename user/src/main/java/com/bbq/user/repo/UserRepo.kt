package com.bbq.user.repo

import com.bbq.base.rository.BaseRepository
import com.bbq.base.rository.StateLiveData
import com.bbq.user.api.UserApi
import com.bbq.user.bean.IntegralBean

class UserRepo(val api: UserApi) : BaseRepository() {
    suspend fun getInterData(data: StateLiveData<IntegralBean>) {
        executeRequest({ api.coinUserInfo() }, data)
    }
}