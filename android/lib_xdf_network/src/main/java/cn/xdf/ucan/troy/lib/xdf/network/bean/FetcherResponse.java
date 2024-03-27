package cn.xdf.ucan.troy.lib.xdf.network.bean;

import com.google.gson.annotations.SerializedName;

/**
 * @author hulijia
 * @createDate 2020/9/1
 * @description FetcherResponse
 */
public class FetcherResponse<T> {
    @SerializedName(value = "haltTimestamp", alternate = {"HaltTimestamp"})
    public String haltTimestamp = "";

    @SerializedName(value = "status", alternate = {"Status"})
    public String status = "-1";

    @SerializedName(value = "message", alternate = {"Message", "msg"})
    public String message = "";

    @SerializedName(value = "errorCode", alternate = {"ErrorCode", "errorcode"})
    public String errorCode = "";

    @SerializedName(value = "data", alternate = {"Data"})
    public T data;

    @Override
    public String toString() {
        return "FetcherResponse{" +
                "haltTimestamp='" + haltTimestamp + '\'' +
                ", status='" + status + '\'' +
                ", message='" + message + '\'' +
                ", errorCode='" + errorCode + '\'' +
                ", data=" + data +
                '}';
    }
}