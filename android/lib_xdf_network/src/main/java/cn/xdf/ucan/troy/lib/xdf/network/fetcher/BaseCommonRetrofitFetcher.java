package cn.xdf.ucan.troy.lib.xdf.network.fetcher;

import java.util.ArrayList;
import java.util.List;

import cn.xdf.ucan.troy.lib.network.converter.CustomGsonConverterFactory;
import cn.xdf.ucan.troy.lib.network.fetcher.BaseRetrofitFetcher;
import cn.xdf.ucan.troy.lib.network.fetcher.IResponseFilterValid;
import cn.xdf.ucan.troy.lib.utils.DebugUtils;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

/**
 * @author hulijia
 * @createDate 2021/9/17
 * @description BaseCommonRetrofitFetcher
 */
public abstract class BaseCommonRetrofitFetcher<T, U> extends BaseRetrofitFetcher<T, U> {

    @Override
    protected Retrofit createRetrofit() {
        return super.createRetrofit();
    }

    @Override
    protected Retrofit createRetrofit(OkHttpClient okHttpClient) {
        Retrofit retrofit = super.createRetrofit(okHttpClient);
        return retrofit.newBuilder()
                .addConverterFactory(CustomGsonConverterFactory.create(getResponseFilterValid()))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
    }

    @Override
    protected Retrofit createRetrofitNoInterceptor() {
        return super.createRetrofitNoInterceptor();
    }

    @Override
    protected String getBaseUrl() {
        return DebugUtils.BaseUrl;
    }

    @Override
    protected List<Interceptor> getInterceptorList() {
//        List<Interceptor> interceptorList = new ArrayList<>();
//        interceptorList.add(new HeaderInterceptor());
//        interceptorList.add(new LoginInterceptor());
        return null;
    }

    /**
     * 如果bean结构体有参数(目前只支持第一层)为null，将会导致整个bean无法正常获取，该方法可以获取其它正常的bean参数
     *
     * @return IResponseFilterValid
     */
    protected IResponseFilterValid getResponseFilterValid() {
        return null;
    }

}