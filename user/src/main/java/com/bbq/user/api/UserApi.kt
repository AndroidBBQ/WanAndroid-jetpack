package com.bbq.user.api

import com.bbq.net.model.BaseResult
import com.bbq.user.bean.IntegralBean
import retrofit2.http.GET

interface UserApi {

    //获取个人积分，需要登录后访问
    @GET("lg/coin/userinfo/json")
    suspend fun coinUserInfo(): BaseResult<IntegralBean>
}