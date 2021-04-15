package com.soft1851.swl.face.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.List;

/**
 * @author wl_sun
 * @description TODO
 * @Data 2021/4/15
 */
public class JsonUtil {

    // 瀹氫箟jackson瀵硅薄
    private static final ObjectMapper MAPPER = new ObjectMapper();

    /**
     * 灏嗗璞¤浆鎹㈡垚json瀛楃涓�
     *
     * @param data 鍏ュ弬瀵硅薄
     * @return String
     */
    public static String objectToJson(Object data) {
        try {
            String string = MAPPER.writeValueAsString(data);
            return string;
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 灏唈son缁撴灉闆嗚浆鍖栦负瀵硅薄
     *
     * @param jsonData json鏁版嵁
     * @param beanType 瀵硅薄涓殑object绫诲瀷
     * @return <T>
     */
    public static <T> T jsonToPojo(String jsonData, Class<T> beanType) {
        try {
            T t = MAPPER.readValue(jsonData, beanType);
            return t;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 灏唈son鏁版嵁杞崲鎴恜ojo瀵硅薄list
     *
     * @param jsonData 鍏ュ弬
     * @param beanType 鍏ュ弬
     * @return <T>
     */
    public static <T> List<T> jsonToList(String jsonData, Class<T> beanType) {
        JavaType javaType = MAPPER.getTypeFactory().constructParametricType(List.class, beanType);
        try {
            List<T> list = MAPPER.readValue(jsonData, javaType);
            return list;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}