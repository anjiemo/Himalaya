package com.smart.himalaya.utils;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.greenrobot.greendao.converter.PropertyConverter;

import java.util.List;

public class ListUniversalCastConvert<T> implements PropertyConverter<List<T>,String> {
    @Override
    public List<T> convertToEntityProperty(String databaseValue) {
        if (databaseValue != null) {
            return new Gson().fromJson(databaseValue, new TypeToken<List<T>>() {
            }.getType());
        }
        return null;
    }

    @Override
    public String convertToDatabaseValue(List<T> entityProperty) {
        if (entityProperty != null) {
            return new Gson().toJson(entityProperty);
        }
        return null;
    }
}
