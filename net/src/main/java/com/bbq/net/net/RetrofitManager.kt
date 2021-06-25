package com.bbq.net.net

import com.bbq.net.cookie.NetCookieJar
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.net.CookieManager
import java.util.concurrent.TimeUnit

//管理retrofit
class RetrofitManager private constructor() {


    private var retrofit: Retrofit

    companion object {
        val instance: RetrofitManager by lazy { RetrofitManager() }
    }

    init {
        retrofit = Retrofit.Builder()
            .client(initClient())
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl("https://www.wanandroid.com")
            .build()
    }

    private fun initClient(): OkHttpClient {
        return OkHttpClient.Builder()
            .callTimeout(15, TimeUnit.SECONDS)
            .connectTimeout(15, TimeUnit.SECONDS)
            .readTimeout(15, TimeUnit.SECONDS)
            .writeTimeout(15, TimeUnit.SECONDS)
            .retryOnConnectionFailure(true)
            .followRedirects(false)
//            .cache(Cache(BaseApp.cacheDir, Long.MAX_VALUE))
            .cookieJar(NetCookieJar(CookieManager()))
            .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
            .build()
    }

    fun <T> create(service: Class<T>): T {
        return retrofit.create(service)
    }
}