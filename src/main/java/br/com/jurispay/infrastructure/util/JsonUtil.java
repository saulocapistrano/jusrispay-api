package br.com.jurispay.infrastructure.util;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Map;

public final class JsonUtil {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    private JsonUtil() {
    }

    public static Integer tryExtractInt(String json, String fieldName) {
        if (json == null || json.isBlank() || fieldName == null || fieldName.isBlank()) {
            return null;
        }

        try {
            Map<String, Object> map = MAPPER.readValue(json, new TypeReference<>() {
            });
            Object value = map.get(fieldName);
            if (value == null) {
                return null;
            }
            if (value instanceof Integer intValue) {
                return intValue;
            }
            if (value instanceof Number number) {
                return number.intValue();
            }
            if (value instanceof String str) {
                return Integer.valueOf(str);
            }
            return null;
        } catch (Exception ignored) {
            return null;
        }
    }
}
