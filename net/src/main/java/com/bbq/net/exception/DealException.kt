package com.bbq.net.exception

import android.net.ParseException
import com.google.gson.JsonParseException

import org.json.JSONException
import retrofit2.HttpException

import java.net.SocketException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import java.net.UnknownServiceException
import javax.net.ssl.SSLHandshakeException

object DealException {

    fun handlerException(t: Throwable): ResultException {
        val ex: ResultException
        if (t is ResultException) {
            ex = t
        } else if (t is HttpException) {
            ex = when (t.code()) {
                ApiCode.UNAUTHORIZED,
                ApiCode.FORBIDDEN,
                    //权限错误，需要实现
                ApiCode.NOT_FOUND -> ResultException(
                    t.code().toString(),
                    "网络错误"
                )
                ApiCode.REQUEST_TIMEOUT,
                ApiCode.GATEWAY_TIMEOUT -> ResultException(
                    t.code().toString(),
                    "网络连接超时"
                )
                ApiCode.INTERNAL_SERVER_ERROR,
                ApiCode.BAD_GATEWAY,
                ApiCode.SERVICE_UNAVAILABLE -> ResultException(
                    t.code().toString(),
                    "服务器错误"
                )
                else -> ResultException(t.code().toString(), "网络错误")
            }
        } else if (t is JsonParseException
            || t is JSONException
            || t is ParseException
        ) {
            ex = ResultException(
                ApiCode.PARSE_ERROR,
                "解析错误"
            )
        } else if (t is SocketException) {
            ex = ResultException(
                ApiCode.REQUEST_TIMEOUT.toString(),
                "网络连接错误，请重试"
            )
        } else if (t is SocketTimeoutException) {
            ex = ResultException(
                ApiCode.REQUEST_TIMEOUT.toString(),
                "网络连接超时"
            )
        } else if (t is SSLHandshakeException) {
            ex = ResultException(
                ApiCode.SSL_ERROR,
                "证书验证失败"
            )
            return ex
        } else if (t is UnknownHostException) {
            ex = ResultException(
                ApiCode.UNKNOW_HOST,
                "网络错误，请切换网络重试"
            )
            return ex
        } else if (t is UnknownServiceException) {
            ex = ResultException(
                ApiCode.UNKNOW_HOST,
                "网络错误，请切换网络重试"
            )
        } else if (t is NumberFormatException) {
            ex = ResultException(
                ApiCode.UNKNOW_HOST,
                "数字格式化异常"
            )
        } else {
            ex = ResultException(
                ApiCode.UNKNOWN,
                "未知错误"
            )
        }
        return ex
    }
}