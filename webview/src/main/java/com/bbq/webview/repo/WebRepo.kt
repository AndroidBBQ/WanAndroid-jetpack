package com.bbq.webview.repo

import com.bbq.base.rository.BaseRepository
import com.bbq.net.model.ResultState
import com.bbq.webview.api.WebApi

class WebRepo(val webApi: WebApi) : BaseRepository() {
    suspend fun collect(id: Int?): Boolean {
        val result = callRequest { handleResponse(webApi.collect(id)) }
        return result is ResultState.Success
    }

    suspend fun unCollect(id: Int?): Boolean {
        val result = callRequest { handleResponse(webApi.unCollect(id)) }
        return result is ResultState.Success
    }
}