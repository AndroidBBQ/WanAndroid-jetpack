package com.bbq.base.rository

import android.util.Log
import com.bbq.net.exception.DealException

import com.bbq.net.exception.ResultException
import com.bbq.net.model.BaseResult
import com.bbq.net.model.DataStatus
import com.bbq.net.model.ResultState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.*

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
            if (response.errorCode != 0) {
                //返回的不成功
                errorBlock?.let { it() }
                //结果回调
                ResultState.Error(
                    ResultException(
                        response.errorCode.toString(),
                        response.errorMsg ?: ""
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

    suspend fun <T : Any> executeRequest(
        block: suspend () -> BaseResult<T>,
        stateLiveData: StateLiveData<T>
    ) {
        var baseResp = BaseResult<T>()
        try {
            baseResp.dataStatus = DataStatus.STATE_LOADING
            stateLiveData.postValue(baseResp)
            //将结果复制给baseResp
            baseResp = block.invoke()
            if (baseResp.errorCode == 0) {
                baseResp.dataStatus = DataStatus.STATE_SUCCESS
            } else {
                //服务器请求错误
                baseResp.dataStatus = DataStatus.STATE_ERROR
                baseResp.exception = ResultException(
                    baseResp.errorCode.toString(),
                    baseResp.errorMsg ?: ""
                )
            }
        } catch (e: Exception) {
            //非后台返回错误，捕获到的异常
            baseResp.dataStatus = DataStatus.STATE_ERROR
            baseResp.exception = DealException.handlerException(e)
        } finally {
            stateLiveData.postValue(baseResp)
        }
    }


    /**
     * 方式二：结合Flow请求数据。
     * 根据Flow的不同请求状态，如onStart、onEmpty、onCompletion等设置baseResp.dataState状态值，
     * 最后通过stateLiveData分发给UI层。
     *
     * @param block api的请求方法
     * @param stateLiveData 每个请求传入相应的LiveData，主要负责网络状态的监听
     */
    suspend fun <T : Any> executeReqWithFlow(
        block: suspend () -> BaseResult<T>,
        stateLiveData: StateLiveData<T>
    ) {
        var baseResp = BaseResult<T>()
        flow {
            val respResult = block.invoke()
            baseResp = respResult
            emit(respResult)
        }
            .flowOn(Dispatchers.IO)
            .onStart {
                baseResp.dataStatus = DataStatus.STATE_LOADING
                stateLiveData.postValue(baseResp)
            }
            .catch { exception ->
                run {
                    //非后台返回错误，捕获到的异常
                    baseResp.dataStatus = DataStatus.STATE_ERROR
                    baseResp.exception = DealException.handlerException(exception)
                    stateLiveData.postValue(baseResp)
                }
            }
            .collect {
                if (baseResp.errorCode == 0) {
                    baseResp.dataStatus = DataStatus.STATE_SUCCESS
                } else {
                    //服务器请求错误
                    baseResp.dataStatus = DataStatus.STATE_ERROR
                    baseResp.exception = ResultException(
                        baseResp.errorCode.toString(),
                        baseResp.errorMsg ?: ""
                    )
                }
                stateLiveData.postValue(baseResp)
            }
    }

}