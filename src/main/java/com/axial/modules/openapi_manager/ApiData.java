package com.axial.modules.openapi_manager;

import com.axial.modules.openapi_manager.model.ApiHeader;
import com.axial.modules.openapi_manager.model.config.HeaderConfig;
import com.axial.modules.openapi_manager.model.config.SecurityHeaderConfig;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

public class ApiData {

    private ApiData() { }

    private static Map<String, ApiHeader> headers = new ConcurrentHashMap<>();


    public static Map<String, ApiHeader> getHeaders() {
        return headers;
    }

    public static ApiHeader getHeader(String headerName) {
        return headers.get(headerName);
    }


    public static void addHeaderIfNotExist(List<ApiHeader> apiHeaders) {
        if (OpenApiUtils.isEmpty(apiHeaders)) {
            return;
        }
        apiHeaders.forEach(hd -> addHeaderIfNotExist(hd));
    }

    public static void addHeaderIfNotExist(ApiHeader apiHeader) {
        if (Objects.isNull(apiHeader)) {
            return;
        }
        headers.putIfAbsent(apiHeader.getName(), apiHeader);
    }

    static void addSecurityHeadersToMainHeadersIfNotExist(List<SecurityHeaderConfig> securityHeaderConfigs) {
        if (OpenApiUtils.isEmpty(securityHeaderConfigs)) {
            return;
        }
        addHeaderIfNotExist(OpenApiUtils.convertSecurityHeaderConfigsToApiHeaders(securityHeaderConfigs));
    }

    static void addSecurityHeaderToMainHeadersIfNotExist(SecurityHeaderConfig securityHeaderConfig) {
        addHeaderIfNotExist(OpenApiUtils.convertSecurityHeaderConfigToApiHeader(securityHeaderConfig));
    }

    static void addHeaderConfigsToMainHeadersIfNotExist(List<HeaderConfig> headerConfigs) {
        if (OpenApiUtils.isEmpty(headerConfigs)) {
            return;
        }
        addHeaderIfNotExist(OpenApiUtils.convertHeaderConfigsToApiHeaders(headerConfigs));
    }

    static void addHeaderIfNotExist(HeaderConfig headerConfig) {
        addHeaderIfNotExist(OpenApiUtils.convertHeaderConfigToApiHeader(headerConfig));
    }

}
