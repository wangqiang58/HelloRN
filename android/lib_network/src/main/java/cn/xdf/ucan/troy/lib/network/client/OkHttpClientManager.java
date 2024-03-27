package cn.xdf.ucan.troy.lib.network.client;

import java.util.concurrent.TimeUnit;

import cn.xdf.ucan.troy.lib.network.utils.SslUtils;
import okhttp3.OkHttpClient;

/**
 * @author hulijia
 * @createDate 2020/9/1
 * @description OkHttpClientManager
 */
public class OkHttpClientManager {
    private static final String NETWORK = "OKHTTP_NETWORK";
    private static volatile OkHttpClient sInstance;
    private static boolean sIsLog = false;

    private OkHttpClientManager() {
    }

    public static void setsIsLog(boolean isLog) {
        sIsLog = isLog;
    }

    public static boolean getIsLog() {
        return sIsLog;
    }

    public static OkHttpClient getInstance() {
        if (sInstance == null) {
            synchronized (OkHttpClientManager.class) {
                if (sInstance == null) {
                    sInstance = build();
                }
            }
        }

        return sInstance;
    }

    public static OkHttpClient getNewInstance() {
        return build();
    }

    private static OkHttpClient build() {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();

        return builder
                .sslSocketFactory(SslUtils.createSSLSocketFactory(), new SslUtils.MyTrustManager())
                .hostnameVerifier(new SslUtils.TrustAllHostnameVerifier())
                .connectTimeout(60, TimeUnit.SECONDS)
                .readTimeout(100,TimeUnit.SECONDS)
                .writeTimeout(60,TimeUnit.SECONDS)
                .retryOnConnectionFailure(true)
                .build();
    }

}
