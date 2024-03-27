package cn.xdf.ucan.troy.lib.network.fetcher;

import io.reactivex.Observable;

/**
 * @author hulijia
 * @createDate 2020/9/1
 * @description IFetcher
 */
public interface IFetcher<T, U> {

    /**
     * 通用请求网络方法，适合于只有一个方法的场景
     *
     * @param params params
     * @return Observable<U>
     */
    Observable<U> fetch(T params);

}