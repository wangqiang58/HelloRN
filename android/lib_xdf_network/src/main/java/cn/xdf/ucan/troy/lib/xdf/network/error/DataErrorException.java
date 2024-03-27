package cn.xdf.ucan.troy.lib.xdf.network.error;

/**
 * @author hulijia
 * @createDate 2021/9/6
 * @description DataErrorException
 */
public class DataErrorException extends Exception {
    private final String status;
    private final String errorCode;
    private final String msg;

    public DataErrorException(String msg) {
        this(null, null, msg);
    }

    public DataErrorException(String status, String errorCode, String msg) {
        this.status = status;
        this.errorCode = errorCode;
        this.msg = msg;
    }

    public String getStatus() {
        return status;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public String getMsg() {
        return msg;
    }
}