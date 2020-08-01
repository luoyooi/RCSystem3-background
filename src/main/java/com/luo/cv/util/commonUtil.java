package com.luo.cv.util;

import com.luo.cv.bean.Movie;

import java.lang.reflect.Field;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author shkstart
 * @create 2020-07-29 13:35
 */
public class commonUtil {
    // 将对象转换成map
    public static <T> Map<String , Object>  exchangeObjectToMap(T t) {
        Class<?> clazz = t.getClass();

        Field[] fields = clazz.getDeclaredFields();
        Map<String, Object> map = new LinkedHashMap<>(fields.length);
        for (Field field : fields) {
            field.setAccessible(true);
            try {
                map.put(field.getName(), field.get(t));
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }

        return map;
    }
}
