package com.izbean.blogsearch.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class MapperUtils {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static <T> T readValueOrThrow(String value, Class<T> valueClass) {

        try {
            return objectMapper.readValue(value, valueClass);
        } catch (Exception e) {
            log.error("objectMapper Parsing Error: {}", e.getMessage());
        }

        return null;
    }

}
