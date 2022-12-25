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

    static List<HeaderConfig> convertApiHeadersToHeaderConfigs(List<ApiHeader> apiHeaders) {
        if (isEmpty(apiHeaders)) {
            return Collections.emptyList();
        }
        return apiHeaders.stream().map(OpenApiUtils::convertApiHeaderToHeaderConfig).collect(Collectors.toList());
    }

    static HeaderConfig convertApiHeaderToHeaderConfig(ApiHeader apiHeader) {
        if (Objects.isNull(apiHeader)) {
            return null;
        }
        final HeaderConfig headerConfig = new HeaderConfig();
        headerConfig.setName(apiHeader.getName());
        headerConfig.setRequired(apiHeader.isRequired());
        headerConfig.setDescription(apiHeader.getDescription());
        headerConfig.setDefaultValue(apiHeader.getDefaultValue());
        headerConfig.setExample(apiHeader.getDefaultValue());
        return headerConfig;
    }

    static List<ApiHeader> convertHeaderConfigsToApiHeaders(List<HeaderConfig> headerConfigs) {
        if (isEmpty(headerConfigs)) {
            return Collections.emptyList();
        }
        return headerConfigs.stream().map(OpenApiUtils::convertHeaderConfigToApiHeader).collect(Collectors.toList());
    }

    static ApiHeader convertHeaderConfigToApiHeader(HeaderConfig headerConfig) {
        if (Objects.isNull(headerConfig)) {
            return null;
        }
        final ApiHeader apiHeader = new ApiHeader();
        apiHeader.setKey(headerConfig.getName());
        apiHeader.setName(headerConfig.getName());
        apiHeader.setRequired(headerConfig.getRequired());
        apiHeader.setDescription(headerConfig.getDescription());
        apiHeader.setDefaultValue(headerConfig.getDefaultValue());
        apiHeader.setDefaultApiHeader(false);
        apiHeader.setDefaultSecurityHeader(false);
        return apiHeader;
    }

    static List<ApiHeader> convertSecurityHeaderConfigsToApiHeaders(List<SecurityHeaderConfig> securityHeaderConfigs) {
        if (isEmpty(securityHeaderConfigs)) {
            return Collections.emptyList();
        }
        return securityHeaderConfigs.stream().map(OpenApiUtils::convertSecurityHeaderConfigToApiHeader).collect(Collectors.toList());
    }

    static ApiHeader convertSecurityHeaderConfigToApiHeader(SecurityHeaderConfig securityHeaderConfig) {
        if (Objects.isNull(securityHeaderConfig)) {
            return null;
        }
        final ApiHeader apiHeader = new ApiHeader();
        apiHeader.setKey(securityHeaderConfig.getKey());
        apiHeader.setName(securityHeaderConfig.getName());
        apiHeader.setRequired(false);
        apiHeader.setDescription(securityHeaderConfig.getDescription());
        apiHeader.setDefaultValue(securityHeaderConfig.getExample());
        apiHeader.setDefaultApiHeader(false);
        apiHeader.setDefaultSecurityHeader(false);
        return apiHeader;
    }

    static SecurityHeaderConfig convertApiHeaderToSecurityHeaderConfig(ApiHeader apiHeader) {
        if (Objects.isNull(apiHeader)) {
            return null;
        }
        final SecurityHeaderConfig securityHeader = new SecurityHeaderConfig();
        securityHeader.setKey(apiHeader.getKey());
        securityHeader.setName(apiHeader.getName());
        securityHeader.setExample(apiHeader.getDefaultValue());
        securityHeader.setDescription(apiHeader.getDescription());
        return securityHeader;
    }

}
