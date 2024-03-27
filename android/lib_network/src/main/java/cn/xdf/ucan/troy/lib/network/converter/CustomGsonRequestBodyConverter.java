package cn.xdf.ucan.troy.lib.network.converter;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonWriter;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.charset.StandardCharsets;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okio.Buffer;
import retrofit2.Converter;

/**
 * @author hulijia
 * @createDate 2020/9/1
 * @description CustomGsonRequestBodyConverter
 */
public class CustomGsonRequestBodyConverter<T> implements Converter<T, RequestBody> {
    private static final MediaType MEDIA_TYPE = MediaType.parse("application/json; charset=UTF-8");

    private final Gson mGson;
    private final TypeAdapter<T> mAdapter;

    CustomGsonRequestBodyConverter(Gson gson, TypeAdapter<T> adapter) {
        mGson = gson;
        mAdapter = adapter;
    }

    @Override
    public RequestBody convert(@NotNull T value) throws IOException {
        Buffer buffer = new Buffer();
        Writer writer = new OutputStreamWriter(buffer.outputStream(), StandardCharsets.UTF_8);
        JsonWriter jsonWriter = mGson.newJsonWriter(writer);
        mAdapter.write(jsonWriter, value);
        jsonWriter.close();
        return RequestBody.create(buffer.readByteString(), MEDIA_TYPE);
    }

}