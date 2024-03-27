package cn.xdf.ucan.troy.aispeak.mvvm.repository


import cn.xdf.ucan.troy.lib.network.converter.CustomGsonConverterFactory
import cn.xdf.ucan.troy.lib.network.utils.SslUtils
import cn.xdf.ucan.troy.lib.utils.DebugUtils
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import java.util.concurrent.TimeUnit

class AiRepository<T> {
    private lateinit var retrofit: Retrofit
    public var fetcher: T

    constructor(api: Class<T>) {
        fetcher = createRetrofit(api)
    }

    private fun createRetrofit(api: Class<T>): T {
        var okhttpClient = OkHttpClient.Builder()
            .sslSocketFactory(SslUtils.createSSLSocketFactory(), SslUtils.MyTrustManager())
            .hostnameVerifier(SslUtils.TrustAllHostnameVerifier())
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .retryOnConnectionFailure(true)
            .build();
        retrofit = Retrofit.Builder().baseUrl(DebugUtils.BaseUrl).client(okhttpClient)
            .addConverterFactory(CustomGsonConverterFactory.create(null)).build()
        fetcher = retrofit.create<T>(api)
        return fetcher
    }
}