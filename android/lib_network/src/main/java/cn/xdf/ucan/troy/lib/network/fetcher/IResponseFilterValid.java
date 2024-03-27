package cn.xdf.ucan.troy.lib.network.fetcher;

import java.io.IOException;

/**
 * @author hulijia
 * @createDate 2020/9/1
 * @description IResponseFilterValid
 */
public interface IResponseFilterValid {
    /**
     * 如果bean结构体有参数(目前只支持第一层)为null，将会导致整个bean无法正常获取，该方法可以获取其它正常的bean参数
     *
     * @param response response
     * @return 有效的参数
     * @throws IOException IOException
     */
    String getValidResponse(String response) throws IOException;
}