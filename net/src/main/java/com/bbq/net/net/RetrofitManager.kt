package com.bbq.net.net

import com.bbq.net.cookie.HttpsHelper
import com.bbq.net.cookie.SimpleCookieJar
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.InputStream
import java.util.concurrent.TimeUnit
import javax.net.ssl.HttpsURLConnection
import javax.net.ssl.SSLContext

//管理retrofit
class RetrofitManager private constructor() {


    private var retrofit: Retrofit

    companion object {
        val instance: RetrofitManager by lazy { RetrofitManager() }
    }

    init {
        retrofit = Retrofit.Builder()
            .client(initClient())
            .validateEagerly(true)
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl("https://www.wanandroid.com")
            .build()
    }

    private var hostNames: Array<String>? = null
    private var clientCertificate: InputStream? = null
    private var serverCertificates: Array<InputStream>? = null
    private var clientCertificatePassword: String? = null

    private fun initClient(): OkHttpClient {

        val keyManagers =
            HttpsHelper.prepareKeyManager(clientCertificate, clientCertificatePassword)
        val trustManager = HttpsHelper.prepareX509TrustManager(serverCertificates)
        val sslContext = SSLContext.getInstance("TLS")
        sslContext.init(keyManagers, arrayOf(trustManager), null)

        return OkHttpClient.Builder()
            .callTimeout(15, TimeUnit.SECONDS)
            .connectTimeout(15, TimeUnit.SECONDS)
            .readTimeout(15, TimeUnit.SECONDS)
            .writeTimeout(15, TimeUnit.SECONDS)
            .retryOnConnectionFailure(true)
            .followRedirects(false)
//            .cache(Cache(BaseApp.cacheDir, Long.MAX_VALUE))
            .cookieJar(SimpleCookieJar())
            .hostnameVerifier { hostname, session ->
                if (hostNames != null) {
                    listOf(*hostNames!!)
                        .contains(hostname)
                } else HttpsURLConnection.getDefaultHostnameVerifier().verify(hostname, session)
            }
            .sslSocketFactory(sslContext.socketFactory, trustManager)
            .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
            .build()
    }

    fun <T> create(service: Class<T>): T {
        return retrofit.create(service)
    }
}