package com.unimelbCoder.melbcode.utils;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * 用于对象转字符串或者字符串转相应对象用
 */
public class JsonUtils {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static <T> T toObj(String str, Class<T> cls) {
        try {
            return objectMapper.readValue(str, cls);
        } catch (Exception e){
            throw new UnsupportedOperationException(e);
        }
    }

    public static <T> String toStr(T t) {
        try {
            return objectMapper.writeValueAsString(t);
        } catch (Exception e) {
            throw new UnsupportedOperationException(e);
        }
    }

}
