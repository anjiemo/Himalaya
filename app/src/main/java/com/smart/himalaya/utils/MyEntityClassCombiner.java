package com.smart.himalaya.utils;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Field;

public class MyEntityClassCombiner {

    private static final String TAG = "MyEntityClassCombiner";
    /**
     * 合并实体类
     *
     * @param jsonA
     * @param classA
     * @param jsonB
     * @param classB
     * @return
     * @throws Exception
     */
    public String getMerge(String jsonA, Class classA, String jsonB, Class classB) throws Exception {
        JSONObject jsonObject = new JSONObject();
        parseJson(jsonObject, new JSONObject(jsonA), classA);
        parseJson(jsonObject, new JSONObject(jsonB), classB);
        return jsonObject.toString();
    }

    /**
     * 解析JSON对象
     *
     * @param jsonObject
     * @param jsonObjectA
     * @param aClass
     * @throws JSONException
     */
    private void parseJson(JSONObject jsonObject, JSONObject jsonObjectA, Class<? extends Class> aClass) throws JSONException {
        Field[] declaredFields = aClass.getDeclaredFields();
        for (Field declaredField : declaredFields) {
            declaredField.setAccessible(true);
            String key = declaredField.getName();
            Object value = jsonObjectA.get(key);
            jsonObject.put(key, value);
        }
    }
}
