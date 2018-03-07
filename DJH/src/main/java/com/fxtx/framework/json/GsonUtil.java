package com.fxtx.framework.json;

import com.fxtx.framework.text.StringUtil;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonParser;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author djh-zy
 * @version :1
 * @CreateDate 2015年8月3日 下午1:59:14
 * @description : Gson使用工具类 所有的Gson 的使用 全部通过这个工具类来进行调用
 */
public class GsonUtil {
    private Gson gons;

    public Gson getGson() {
        if (gons == null) {
            GsonBuilder gsonBuilder = new GsonBuilder();
            gons = gsonBuilder.registerTypeHierarchyAdapter(List.class, new JsonDeserializer<List<?>>() {
                @Override
                public List<?> deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
                     if (json.isJsonArray()) {
                        JsonArray array = json.getAsJsonArray();
                        Type itemType = ((ParameterizedType) typeOfT).getActualTypeArguments()[0];
                        List list = new ArrayList<>();
                        for (int i = 0; i < array.size(); i++) {
                            JsonElement element = array.get(i);
                            Object item = context.deserialize(element, itemType);
                            list.add(item);
                        }
                        return list;
                    } else {
                        //和接口类型不符，返回空List
                        return Collections.EMPTY_LIST;
                    }
                }
            }).create();
        }
        return gons;
    }

    /**
     * 解析一个数组
     *
     * @param json 要解析的json
     * @return
     * @type 要解析成的类型
     */
    public <T> T getJsonList(String json, GsonType type) {
        if (!StringUtil.isEmpty(json)) {
            Gson gson = getGson();
            try {
                return gson.fromJson(json, type.type);
            } catch (Exception e) {
                return null;
            }
        }
        return null;
    }

    /**
     * 解析一个对象
     *
     * @param json
     * @param classs
     * @return
     */
    public <T> T getJsonObject(String json, Class<T> classs) {
        if (!StringUtil.isEmpty(json)) {
            Gson gson = getGson();
            try {
                return gson.fromJson(json, classs);
            } catch (Exception e) {
                return null;
            }
        } else {
            return null;
        }
    }

    /**
     * 将对象转换成json 对象
     *
     * @param object
     * @return
     */
    public JsonElement getJsonElement(Object object) {
        Gson gson = getGson();
        JsonParser parser = new JsonParser();
        return parser.parse(gson.toJson(object));
    }
}
