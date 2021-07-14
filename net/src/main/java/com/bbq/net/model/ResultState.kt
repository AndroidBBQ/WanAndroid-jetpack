package com.bbq.net.model

import com.bbq.net.exception.ResultException

/**
 * 网络请求结果
 */
sealed class ResultState<out T : Any> {

    //成功
    data class Success<out T : Any>(val data: T?) : ResultState<T>()

    //失败
    data class Error(val exception: ResultException) : ResultState<Nothing>()


}