package cn.xdf.ucan.troy.lib.xdf.network.error;

import android.text.TextUtils;

import cn.xdf.ucan.troy.lib.xdf.network.bean.FetcherResponse;
import cn.xdf.ucan.troy.lib.xdf.network.constant.NetworkConstant;

/**
 * @author hulijia
 * @createDate 2021/9/6
 * @description DataErrorExceptionHelper
 */
public class DataErrorExceptionHelper {

    public static <T> DataErrorException checkFetcherResponse(FetcherResponse<T> response) {
        if (response == null) {
            return new DataErrorException("response null");
        }

        String status = response.status;

        //兼容onErrorReturn场景，其实此时response为null
        if (TextUtils.isEmpty(status)) {
            return new DataErrorException("status null");
        }

        if (!NetworkConstant.STATUS_TYPE.OK.equals(response.status) || response.data == null) {
            return new DataErrorException(status, response.errorCode, response.message);
        }

        return null;
    }

}