package cn.xdf.ucan.troy.lib.network.converter;

import com.google.gson.TypeAdapter;

import java.io.IOException;

import cn.xdf.ucan.troy.lib.network.fetcher.IResponseFilterValid;
import okhttp3.ResponseBody;
import retrofit2.Converter;

/**
 * @author hulijia
 * @createDate 2020/9/1
 * @description CustomGsonResponseBodyConverter
 */
public class CustomGsonResponseBodyConverter<T> implements Converter<ResponseBody, T> {
    private final TypeAdapter<T> mAdapter;
    private final IResponseFilterValid mIResponseFilterValid;

    CustomGsonResponseBodyConverter(TypeAdapter<T> adapter,
                                    IResponseFilterValid responseFilterValid) {
        mAdapter = adapter;
        mIResponseFilterValid = responseFilterValid;
    }

    @Override
    public T convert(ResponseBody responseBody) throws IOException {
        try {
            String response = responseBody.string();
            if (null != mIResponseFilterValid) {
                response = mIResponseFilterValid.getValidResponse(response);
            }
            return mAdapter.fromJson(response);
        } finally {
            responseBody.close();
        }
    }

}