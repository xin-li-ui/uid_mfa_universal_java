package com.uidsecurity.utils;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.uidsecurity.exception.UidException;

public class JSONUtil {

    private static final ObjectMapper mapper = new ObjectMapper();

    static {
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    private JSONUtil() {
    }

    /**
     * object to json
     *
     * @return json
     */
    public static String toJSON(Object object) {
        try {
            return mapper.writeValueAsString(object);
        } catch (Exception e) {
            throw new UidException(e);
        }
    }

    /**
     * json to object
     *
     * @param json
     * @param type
     * @param <T>
     * @return
     */
    public static <T> T toObject(String json, Class<T> type) {
        try {
            return mapper.readValue(json, type);
        } catch (Exception e) {
            throw new UidException(e);
        }
    }
}