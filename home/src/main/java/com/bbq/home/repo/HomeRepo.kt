package com.bbq.home.repo

import com.bbq.base.base.BaseRepository
import com.bbq.home.api.HomeApi
import com.bbq.home.bean.BannerBean
import com.bbq.home.bean.HotKeyBean
import com.bbq.net.model.BaseResult
import com.bbq.net.model.ResultState

class HomeRepo(private val homeApi: HomeApi) : BaseRepository() {


    suspend fun getHotKey(): ResultState<MutableList<HotKeyBean>> {
        return callRequest(call = { handleResponse(homeApi.getHotKey()) })
    }

    suspend fun getBanners():ResultState<MutableList<BannerBean>>{
        return callRequest { handleResponse(homeApi.getBanner()) }
    }
}