package cn.xdf.ucan.troy.lib.network.fetcher;

import android.util.Log;

import androidx.annotation.NonNull;

import java.util.List;

import cn.xdf.ucan.troy.lib.network.client.OkHttpClientManager;
import io.reactivex.Observable;
import io.reactivex.functions.Consumer;
import io.reactivex.plugins.RxJavaPlugins;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;

/**
 * @author hulijia
 * @createDate 2020/9/1
 * @description BaseRetrofitFetcher
 */
public abstract class BaseRetrofitFetcher<T, U> implements IFetcher<T, U> {

    protected Retrofit createRetrofit() {
        return createRetrofit(null);
    }

    protected Retrofit createRetrofit(OkHttpClient okHttpClient) {
        RxJavaPlugins.setErrorHandler(new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) throws Exception {
                throwable.printStackTrace();
            }
        });
        if (okHttpClient == null) {
            okHttpClient = OkHttpClientManager.getInstance();
        }

        OkHttpClient.Builder builder = okHttpClient.newBuilder();

        List<Interceptor> interceptorList = getInterceptorList();
        if (interceptorList != null && !interceptorList.isEmpty()) {
            for (Interceptor interceptor : interceptorList) {
                if (interceptor == null) {
                    continue;
                }

                builder.addInterceptor(interceptor);
            }
        }

        if (OkHttpClientManager.getIsLog()) {
            builder.addInterceptor(new HttpLoggingInterceptor(new HttpLoggingInterceptor.Logger() {
                @Override
                public void log(@NonNull String s) {
                    Log.i("okHttp"," message  == " +s);
                }
            }).setLevel(HttpLoggingInterceptor.Level.BODY));
        }
        //build会生成一个新的OkHttpClient对象，需要重新进行引用
        okHttpClient = builder.build();

        return createBaseRetrofit(okHttpClient);
    }

    protected Retrofit createRetrofitNoInterceptor() {
        return createBaseRetrofit(OkHttpClientManager.getInstance());
    }

    private Retrofit createBaseRetrofit(OkHttpClient client) {
        return new Retrofit.Builder()
                .client(client)
                .baseUrl(getBaseUrl())
                .build();
    }

    @Override
    public Observable<U> fetch(T params) {
        return null;
    }

    protected List<Interceptor> getInterceptorList() {
        return null;
    }

    /**
     * baseUrl
     *
     * @return baseUrl
     */
    protected abstract String getBaseUrl();

}