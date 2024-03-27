package cn.xdf.ucan.troy.lib.xdf.network.bean;

/**
 * @author hulijia
 * @createDate 2020/9/1
 * @description DataSourceBindBean
 */
public final class DataSourceBindBean<S, V> {
    public final S source;
    public final V data;
    public final Throwable error;

    public DataSourceBindBean(S source, V data) {
        this(source, data, null);
    }

    public DataSourceBindBean(S source, V data, Throwable error) {
        this.source = source;
        this.data = data;
        this.error = error;
    }
}