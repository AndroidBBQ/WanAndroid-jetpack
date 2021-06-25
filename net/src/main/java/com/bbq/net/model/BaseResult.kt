package com.bbq.net.model

//返回的基础model
data class BaseResult<out T>(val errorCode: Int, val errorMsg: String, val data: T)