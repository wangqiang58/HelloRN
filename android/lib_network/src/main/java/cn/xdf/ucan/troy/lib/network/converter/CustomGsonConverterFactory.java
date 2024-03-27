package cn.xdf.ucan.troy.lib.network.converter;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapter;
import com.google.gson.reflect.TypeToken;

import org.jetbrains.annotations.NotNull;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import cn.xdf.ucan.troy.lib.network.fetcher.IResponseFilterValid;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Converter;
import retrofit2.Retrofit;

/**
 * @author hulijia
 * @createDate 2020/9/1
 * @description CustomGsonConverterFactory
 */
public class CustomGsonConverterFactory extends Converter.Factory {
    private final Gson mGson;
    private final IResponseFilterValid mResponseFilterValid;

    /**
     * Create an instance using a default {@link Gson} instance for conversion. Encoding to JSON and
     * decoding from JSON (when no charset is specified by a header) will use UTF-8.
     */
    public static CustomGsonConverterFactory create(IResponseFilterValid responseFilterValid) {
        //我们点点看的后台，有的(比如"我的书架")接口要求请求体的json串的字段值为null也要提交过去，就像这样{"subject": null}
        //而Retrofit使用@Body提交入参后，Gson默认会过滤掉值为null的字段，所以这里需要给Gson加上.serializeNulls()方法
        //表示值为mull的字段也会参与序列化，这样传递给后台的json串就是为{"subject": null}了
        Gson gson = new GsonBuilder().serializeNulls().create();
        return create(gson, responseFilterValid);
    }

    /**
     * Create an instance using {@code gson} for conversion. Encoding to JSON and
     * decoding from JSON (when no charset is specified by a header) will use UTF-8.
     */
    public static CustomGsonConverterFactory create(Gson gson,
                                                    IResponseFilterValid responseFilterValid) {
        if (gson == null) {
            throw new NullPointerException("gson == null");
        }
        return new CustomGsonConverterFactory(gson, responseFilterValid);
    }

    private CustomGsonConverterFactory(Gson gson, IResponseFilterValid responseFilterValid) {
        mGson = gson;
        mResponseFilterValid = responseFilterValid;
    }

    @Override
    public Converter<ResponseBody, ?> responseBodyConverter(@NotNull Type type,
                                                            @NotNull Annotation[] annotations,
                                                            @NotNull Retrofit retrofit) {
        TypeAdapter<?> adapter = mGson.getAdapter(TypeToken.get(type));
        return new CustomGsonResponseBodyConverter<>(adapter, mResponseFilterValid);
    }

    @Override
    public Converter<?, RequestBody> requestBodyConverter(@NotNull Type type,
                                                          @NotNull Annotation[] parameterAnnotations,
                                                          @NotNull Annotation[] methodAnnotations,
                                                          @NotNull Retrofit retrofit) {
        TypeAdapter<?> adapter = mGson.getAdapter(TypeToken.get(type));
        return new CustomGsonRequestBodyConverter<>(mGson, adapter);
    }

}