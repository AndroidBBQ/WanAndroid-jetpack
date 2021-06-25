package com.bbq.base.base

import com.bbq.net.exception.DealException

import com.bbq.net.exception.ResultException
import com.bbq.net.model.BaseResult
import com.bbq.net.model.ResultState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.coroutineScope

/**
 * 基础的 Repository
 */
open class BaseRepository {
    /**
     * 请求
     */
    suspend fun <T : Any> callRequest(
        call: suspend () -> ResultState<T>
    ): ResultState<T> {
        return try {
            call()
        } catch (e: Exception) {
            //这里统一处理异常
            e.printStackTrace()
            ResultState.Error(DealException.handlerException(e))
        }
    }

    /**
     * 处理返回结果
     */
    suspend fun <T : Any> handleResponse(
        response: BaseResult<T>,
        successBlock: (suspend CoroutineScope.() -> Unit)? = null,
        errorBlock: (suspend CoroutineScope.() -> Unit)? = null
    ): ResultState<T> {
        return coroutineScope {
            if (response.errorCode == -1) {
                //返回的不成功
                errorBlock?.let { it() }
                //结果回调
                ResultState.Error(
                    ResultException(
                        response.errorCode.toString(),
                        response.errorMsg
                    )
                )
            } else {
                //返回成功
                successBlock?.let { it() }
                //结果回调
                ResultState.Success(response.data)
            }
        }
    }


}