package com.axial.modules.openapi_manager;

import com.axial.modules.openapi_manager.model.ApiHeader;
import com.axial.modules.openapi_manager.model.config.HeaderConfig;
import com.axial.modules.openapi_manager.model.config.SecurityHeaderConfig;

import java.util.*;
import java.util.stream.Collectors;

class OpenApiUtils {

    private OpenApiUtils() { }

    static boolean isEmpty(Map<?, ?> map) {
        return map == null || map.isEmpty();
    }

    static boolean isNotEmpty(Map<?, ?> map) {
        return !isEmpty(map);
    }

    static <K, V> Map<K, V> emptyIfNull(Map<K, V> map) {
        return map == null ? Collections.emptyMap() : map;
    }

    static <T> List<T> emptyIfNull(List<T> list) {
        return list == null ? Collections.emptyList() : list;
    }

    static boolean isEmpty(Collection<?> coll) {
        return coll == null || coll.isEmpty();
    }

    static boolean isNotEmpty(Collection<?> coll) {
        return !isEmpty(coll);
    }

    static SecurityHeaderConfig convertApiHeaderToSecurityHeaderConfig(ApiHeader apiHeader) {
        if (Objects.isNull(apiHeader)) {
            return null;
        }
        return SecurityHeaderConfig
                .builder()
                .key(apiHeader.getKey())
                .name(apiHeader.getName())
                .example(apiHeader.getDefaultValue())
                .description(apiHeader.getDescription())
                .build();
    }

}
