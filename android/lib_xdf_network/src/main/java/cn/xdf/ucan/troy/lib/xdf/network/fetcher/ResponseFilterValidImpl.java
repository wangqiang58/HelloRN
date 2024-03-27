package cn.xdf.ucan.troy.lib.xdf.network.fetcher;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;

import java.io.IOException;
import java.io.StringReader;
import java.util.HashMap;
import java.util.Map;

import cn.xdf.ucan.troy.lib.network.fetcher.IResponseFilterValid;

/**
 * @author hulijia
 * @createDate 2020/9/1
 * @description ResponseFilterValidImpl
 */
public class ResponseFilterValidImpl implements IResponseFilterValid {

    @Override
    public String getValidResponse(String response) throws IOException {
        Gson gson = new Gson();
        JsonReader reader = gson.newJsonReader(new StringReader(response));
        Map<String, Object> objectMap = new HashMap<>(8);
        boolean isValid = true;

        reader.beginObject();
        while (reader.hasNext()) {
            String name = reader.nextName();
            if ("status".equalsIgnoreCase(name)
                    && (reader.peek() == JsonToken.STRING || reader.peek() == JsonToken.NUMBER)) {
                objectMap.put("status", reader.nextString());
            } else if (("message".equalsIgnoreCase(name) || "Msg".equalsIgnoreCase(name))
                    && reader.peek() == JsonToken.STRING) {
                objectMap.put("message", reader.nextString());
            } else if (("errorCode".equalsIgnoreCase(name)) && reader.peek() == JsonToken.STRING) {
                objectMap.put("errorCode", reader.nextString());
            } else if ("traceId".equals(name) && reader.peek() == JsonToken.STRING) {
                objectMap.put("traceId", reader.nextString());
            } else if ("data".equalsIgnoreCase(name)) {
                if (reader.peek() != JsonToken.BEGIN_OBJECT && reader.peek() != JsonToken.NULL) {
                    isValid = false;
                    objectMap.put("data", new Object());
                }
                reader.skipValue();
            } else {
                reader.skipValue();
            }
        }
        reader.endObject();

        if (!isValid) {
            response = gson.toJson(objectMap);
        }

        return response;
    }

}